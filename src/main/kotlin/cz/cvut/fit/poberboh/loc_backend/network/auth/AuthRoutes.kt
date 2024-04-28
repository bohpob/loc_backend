package cz.cvut.fit.poberboh.loc_backend.network.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.di.DaoProvider
import cz.cvut.fit.poberboh.loc_backend.models.User
import cz.cvut.fit.poberboh.loc_backend.security.hashing.HashingService
import cz.cvut.fit.poberboh.loc_backend.security.hashing.SHA256HashingService
import cz.cvut.fit.poberboh.loc_backend.security.hashing.SaltedHash
import cz.cvut.fit.poberboh.loc_backend.security.token.JwtTokenService
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenClaim
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenConfig
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

/**
 * Configures the authentication API.
 *
 * @param tokenConfig The token configuration.
 */
fun Route.configureAuthApi(tokenConfig: TokenConfig) {
    // Initialize the services and DAOs.
    val tokenService = JwtTokenService()
    val hashingService = SHA256HashingService()
    val userDao = DaoProvider.provideUserEntityDao()

    // Configure the routes.
    login(hashingService, userDao, tokenService, tokenConfig)
    register(hashingService, userDao)
    refresh(tokenService, tokenConfig, userDao)
    authenticateRoute()
}

/**
 * Route. Refreshes the access token.
 *
 * @param tokenService The token service.
 * @param tokenConfig The token configuration.
 * @param userDao The user DAO.
 */
fun Route.refresh(
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    userDao: UserDao
) {
    post("refresh") {
        val request = call.receiveNullable<RefreshToken>() ?: return@post call.respond(HttpStatusCode.Conflict)

        if (request.refreshToken == null) {
            return@post call.respond(HttpStatusCode.Conflict)
        }

        try {
            // Decode the JWT.
            val jwt: DecodedJWT = JWT.decode(request.refreshToken)

            // Check if the token is expired.
            if (jwt.expiresAt.before(Date())) {
                return@post call.respond(HttpStatusCode.Conflict)
            }

            val userIdRefreshToken = jwt.getClaim("userId").asString()
            val userId = RefreshTokens.readUserByRefreshToken(request.refreshToken)

            if (userId != null && userIdRefreshToken == userId) {
                val user = userDao.read(userId.toLong())
                    ?: return@post call.respond(HttpStatusCode.Conflict)

                val (token, refreshToken) = createTokens(tokenService, tokenConfig, user)
                return@post call.respond(HttpStatusCode.OK, TokenResponse(token, refreshToken))
            }
        } catch (e: JWTDecodeException) {
            return@post call.respond(HttpStatusCode.Conflict)
        }
        call.respond(HttpStatusCode.BadRequest)
    }
}

/**
 * Route. Registers a new user.
 *
 * @param hashingService The hashing service.
 * @param userDao The user DAO.
 */
fun Route.register(
    hashingService: HashingService,
    userDao: UserDao
) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val validation = validateLoginInput(request.username, request.password)
        if (validation != null) {
            return@post call.respond(HttpStatusCode.Conflict, validation)
        }

        if (userDao.readByUsername(request.username) != null) {
            return@post call.respond(HttpStatusCode.Conflict, "A user with the same username is already registered")
        }

        // Generate the salted hash.
        val saltedHash = hashingService.generateSaltedHash(request.password)

        userDao.create(request.username, saltedHash.hash, saltedHash.salt)
            ?: return@post call.respond(HttpStatusCode.Conflict)

        call.respond(HttpStatusCode.OK, "Registration successful")
    }
}

/**
 * Route. Logs in a user.
 *
 * @param hashingService The hashing service.
 * @param userDao The user DAO.
 * @param tokenService The token service.
 * @param tokenConfig The token configuration.
 */
fun Route.login(
    hashingService: HashingService,
    userDao: UserDao,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        // Validate the input.
        val validation = validateLoginInput(request.username, request.password)
        if (validation != null) {
            return@post call.respond(HttpStatusCode.Conflict, validation)
        }

        val user = userDao.readByUsername(request.username)
            ?: return@post call.respond(HttpStatusCode.Conflict, "Incorrect username or password")

        // Verify the password.
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            return@post call.respond(HttpStatusCode.Conflict, "Invalid password")
        }

        // Create the tokens.
        val (accessToken, refreshToken) = createTokens(tokenService, tokenConfig, user)

        call.respond(HttpStatusCode.OK, TokenResponse(accessToken, refreshToken))
    }
}

/**
 * Creates the access and refresh tokens.
 */
private fun createTokens(tokenService: TokenService, tokenConfig: TokenConfig, user: User): TokenResponse {
    val accessToken = tokenService.createAccessToken(
        tokenConfig,
        TokenClaim("userId", user.id.toString()),
        TokenClaim("username", user.username)
    )

    val refreshToken = tokenService.createRefreshToken(
        tokenConfig,
        TokenClaim("userId", user.id.toString())
    )
    // Store the refresh token.
    RefreshTokens.addRefreshToken(user.id.toString(), refreshToken)

    return TokenResponse(accessToken, refreshToken)
}

/**
 * Validates the login input.
 *
 * @param username The username.
 * @param password The password.
 * @return The error message if the input is invalid, null otherwise.
 */
private fun validateLoginInput(username: String, password: String): String? {
    if (username.isEmpty() || password.isEmpty()) {
        return "Username and password must not be empty"
    } else if (password.length < 8) {
        return "Password must be at least 8 characters"
    } else if (username.length !in 1..25) {
        return "Username must be between 1 and 25 characters"
    } else if (password.contains(" ") || username.contains(" ")) {
        return "Username and password must not contain spaces"
    } else if (!username.matches(Regex("^[a-zA-Z0-9]*$")) ||
        !password.matches(Regex("^[a-zA-Z0-9]*$"))
    ) {
        return "Username must contain only english letters and numbers"
    }
    return null
}

/**
 * Represents the authentication request.
 */
fun Route.authenticateRoute() {
    authenticate {
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}