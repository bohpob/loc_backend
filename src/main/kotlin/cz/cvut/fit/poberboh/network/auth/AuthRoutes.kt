package cz.cvut.fit.poberboh.network.auth

import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.security.hashing.HashingService
import cz.cvut.fit.poberboh.security.hashing.SaltedHash
import cz.cvut.fit.poberboh.security.token.TokenClaim
import cz.cvut.fit.poberboh.security.token.TokenConfig
import cz.cvut.fit.poberboh.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.register(
    hashingService: HashingService,
    userEntityDao: UserEntityDao
) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userEntityDao.readUserByUsername(request.username) != null) {
            call.respond(HttpStatusCode.Conflict, "A user with the same username is already registered")
            return@post
        }

        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.Conflict, "Username or password is empty")
            return@post
        }

        if (request.password.length < 8) {
            call.respond(HttpStatusCode.Conflict, "Password is too short")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = userEntityDao.createUser(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        if (user == null) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK, "Registration successful")
    }
}

fun Route.login(
    hashingService: HashingService,
    userEntityDao: UserEntityDao,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.Conflict, "Username or password is empty")
            return@post
        }

        val user = userEntityDao.readUserByUsername(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Invalid password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            ),
            TokenClaim(
                name = "username",
                value = user.username
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = TokenResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}

//@todo logout