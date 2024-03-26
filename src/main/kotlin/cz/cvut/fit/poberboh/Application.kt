package cz.cvut.fit.poberboh

import cz.cvut.fit.poberboh.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val tokenConfig = environment.createTokenConfig()
    configureDatabase()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
    configureMonitoring()
    configureSerialization()
}