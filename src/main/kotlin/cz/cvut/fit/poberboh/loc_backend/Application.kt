package cz.cvut.fit.poberboh.loc_backend

import cz.cvut.fit.poberboh.loc_backend.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val tokenConfig = environment.createTokenConfig()
    val databaseConfig = environment.createDatabaseConfig()
    configureDatabase(databaseConfig)
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
    configureMonitoring()
    configureSerialization()
}