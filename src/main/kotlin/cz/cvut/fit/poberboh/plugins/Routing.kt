package cz.cvut.fit.poberboh.plugins

import cz.cvut.fit.poberboh.di.DaoProvider
import cz.cvut.fit.poberboh.network.auth.configureAuthApi
import cz.cvut.fit.poberboh.network.incident.configureIncidentApi
import cz.cvut.fit.poberboh.network.user.configureUserApi
import cz.cvut.fit.poberboh.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    tokenConfig: TokenConfig
) {
    routing {
        route("auth") {
            configureAuthApi(tokenConfig)
        }

        route("api/v1") {
            configureUserApi(DaoProvider.provideUserEntityDao())

            route("incidents") {
                configureIncidentApi(
                    DaoProvider.provideUserEntityDao(),
                    DaoProvider.provideIncidentDao(),
                    DaoProvider.provideGPSIncidentDao()
                )
            }
        }
    }
}
