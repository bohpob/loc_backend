package cz.cvut.fit.poberboh.loc_backend.network.user

import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.di.DaoProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureUserApi() {
    val userDao = DaoProvider.provideUserEntityDao()

    authenticate {
        readCurrentUser(userDao)
    }
}

fun Route.readCurrentUser(userDao: UserDao) {
    get("users/me") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)
        val username = principal?.getClaim("username", String::class)

        if (userId != null) {
            val user = userDao.readById(userId.toLong())
            if (user != null) {
                call.respond(HttpStatusCode.OK, UserResponse(username = username))
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
        }
    }
}