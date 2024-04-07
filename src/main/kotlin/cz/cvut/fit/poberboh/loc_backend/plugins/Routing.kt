package cz.cvut.fit.poberboh.loc_backend.plugins

import cz.cvut.fit.poberboh.loc_backend.di.DaoProvider
import cz.cvut.fit.poberboh.loc_backend.network.auth.configureAuthApi
import cz.cvut.fit.poberboh.loc_backend.network.incident.configureIncidentApi
import cz.cvut.fit.poberboh.loc_backend.network.user.configureUserApi
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenConfig
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
                    DaoProvider.provideLocationDao()
                )
            }
        }
    }
}
