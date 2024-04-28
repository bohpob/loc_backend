package cz.cvut.fit.poberboh.loc_backend

import cz.cvut.fit.poberboh.loc_backend.plugins.*
import io.ktor.server.application.*

/**
 * The main entry point of the application.
 * It starts the Ktor server using Netty.
 *
 * @param args The command-line arguments.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Configures the Ktor application module.
 * This function sets up various components of the application,
 * such as token configuration, database configuration, security,
 * routing, monitoring, and serialization.
 */
fun Application.module() {
    val tokenConfig = environment.createTokenConfig()
    val databaseConfig = environment.createDatabaseConfig()
    configureDatabase(databaseConfig)
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
    configureMonitoring()
    configureSerialization()
}