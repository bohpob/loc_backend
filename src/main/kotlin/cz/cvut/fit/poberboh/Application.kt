package cz.cvut.fit.poberboh

import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.data.users.UserEntityDaoImpl
import cz.cvut.fit.poberboh.database.DatabaseSingleton
import cz.cvut.fit.poberboh.plugins.*
import cz.cvut.fit.poberboh.security.hashing.SHA256HashingService
import cz.cvut.fit.poberboh.security.token.JwtTokenService
import cz.cvut.fit.poberboh.security.token.TokenConfig
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()

    val userEntityDao: UserEntityDao = UserEntityDaoImpl()
    val tokenService = JwtTokenService()
    val tokenConfig = environment.createTokenConfig()
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureRouting(userEntityDao, hashingService, tokenService, tokenConfig)
    configureMonitoring()
    configureSerialization()
}

fun ApplicationEnvironment.createTokenConfig(): TokenConfig {
    return TokenConfig(
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        expiresIn = config.property("jwt.expiresIn").getString().toLong(),
        secret = config.property("jwt.secret").getString()
    )
}