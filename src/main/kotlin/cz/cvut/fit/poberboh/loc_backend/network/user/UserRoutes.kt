package cz.cvut.fit.poberboh.loc_backend.network.user

import cz.cvut.fit.poberboh.loc_backend.data.users.UserEntityDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureUserApi(userEntityDao: UserEntityDao) {
    authenticate {
        getUser(userEntityDao)
    }
}

fun Route.getUser(userEntityDao: UserEntityDao) {
    get("users/me") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)
        val username = principal?.getClaim("username", String::class)

        if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
            call.respond(
                status = HttpStatusCode.OK,
                UserResponse(
                    username = username
                )
            )
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
        }
    }
}