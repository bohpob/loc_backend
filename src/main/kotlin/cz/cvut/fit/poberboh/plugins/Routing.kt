package cz.cvut.fit.poberboh.plugins

import cz.cvut.fit.poberboh.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.data.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.data.incidents.gps.GPSIncidentDao
import cz.cvut.fit.poberboh.data.incidents.gps.GPSIncidentDaoImpl
import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.data.users.UserEntityDaoImpl
import cz.cvut.fit.poberboh.network.auth.authenticate
import cz.cvut.fit.poberboh.network.auth.login
import cz.cvut.fit.poberboh.network.auth.register
import cz.cvut.fit.poberboh.network.incident.configureIncidentApi
import cz.cvut.fit.poberboh.network.user.configureUserApi
import cz.cvut.fit.poberboh.security.hashing.HashingService
import cz.cvut.fit.poberboh.security.token.TokenConfig
import cz.cvut.fit.poberboh.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    val userEntityDao: UserEntityDao = UserEntityDaoImpl()
    val incidentDao: IncidentDao = IncidentDaoImpl()
    val gpsIncidentDao: GPSIncidentDao = GPSIncidentDaoImpl()

    routing {
        // Auth
        route("auth") {
            login(hashingService, userEntityDao, tokenService, tokenConfig)
            register(hashingService, userEntityDao)
            authenticate()
        }

        route("api/v1") {
            configureUserApi(userEntityDao)

            route("incidents") {
                configureIncidentApi(userEntityDao, incidentDao, gpsIncidentDao)
            }
        }
    }
}
