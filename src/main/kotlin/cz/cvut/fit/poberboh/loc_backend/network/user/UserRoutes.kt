package cz.cvut.fit.poberboh.loc_backend.network.user

import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.di.DaoProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Configures the user API.
 */
fun Route.configureUserApi() {
    // Initialize the user DAO.
    val userDao = DaoProvider.provideUserEntityDao()

    authenticate { // Routes that require authentication.
        readCurrentUser(userDao)
    }
}

/**
 * Route. Reads the current user.
 *
 * @param userDao The user DAO.
 */
fun Route.readCurrentUser(userDao: UserDao) {
    // Read the current user.
    get("users/me") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)
        val username = principal?.getClaim("username", String::class)

        if (userId != null) {
            val user = userDao.read(userId.toLong())
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