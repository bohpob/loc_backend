package cz.cvut.fit.poberboh.network.user

import cz.cvut.fit.poberboh.data.users.UserEntityDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureUserApi(userEntityDao: UserEntityDao) {
    authenticate {
        get("me") {
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
}