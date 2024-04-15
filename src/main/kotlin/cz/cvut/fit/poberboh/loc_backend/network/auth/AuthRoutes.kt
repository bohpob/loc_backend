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

fun Route.configureAuthApi(tokenConfig: TokenConfig) {
    val tokenService = JwtTokenService()
    val hashingService = SHA256HashingService()
    val userDao = DaoProvider.provideUserEntityDao()

    login(hashingService, userDao, tokenService, tokenConfig)
    register(hashingService, userDao)
    refresh(tokenService, tokenConfig, userDao)
    authenticateRoute()
}

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
            val jwt: DecodedJWT = JWT.decode(request.refreshToken)

            if (jwt.expiresAt.before(Date())) {
                return@post call.respond(HttpStatusCode.Conflict)
            }

            val userIdRefreshToken = jwt.getClaim("userId").asString()
            val userId = RefreshTokens.readUserByRefreshToken(request.refreshToken)

            if (userId != null && userIdRefreshToken == userId) {
                val user = userDao.readById(userId.toLong())
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

fun Route.register(
    hashingService: HashingService,
    userDao: UserDao
) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (userDao.readByUsername(request.username) != null) {
            return@post call.respond(HttpStatusCode.Conflict, "A user with the same username is already registered")
        }

        if (request.username.isBlank() || request.password.isBlank()) {
            return@post call.respond(HttpStatusCode.Conflict, "Username or password is empty")
        }

        if (request.password.length < 8) {
            return@post call.respond(HttpStatusCode.Conflict, "Password is too short")
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)

        userDao.create(request.username, saltedHash.hash, saltedHash.salt)
            ?: return@post call.respond(HttpStatusCode.Conflict)

        call.respond(HttpStatusCode.OK, "Registration successful")
    }
}

fun Route.login(
    hashingService: HashingService,
    userDao: UserDao,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (request.username.isBlank() || request.password.isBlank()) {
            return@post call.respond(HttpStatusCode.Conflict, "Username or password is empty")
        }

        val user = userDao.readByUsername(request.username)
            ?: return@post call.respond(HttpStatusCode.Conflict, "Incorrect username or password")

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

        val (accessToken, refreshToken) = createTokens(tokenService, tokenConfig, user)

        call.respond(HttpStatusCode.OK, TokenResponse(accessToken, refreshToken))
    }
}

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
    RefreshTokens.addRefreshToken(user.id.toString(), refreshToken)

    return TokenResponse(accessToken, refreshToken)
}

fun Route.authenticateRoute() {
    authenticate {
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}