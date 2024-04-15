package cz.cvut.fit.poberboh.loc_backend.plugins

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseConfig
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenConfig
import io.ktor.server.application.*

fun ApplicationEnvironment.createTokenConfig(): TokenConfig {
    return TokenConfig(
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        expiresIn = config.property("jwt.expiresIn").getString().toLong(),
        refreshIn = config.property("jwt.refreshIn").getString().toLong(),
        secret = config.property("jwt.secret").getString()
    )
}

fun ApplicationEnvironment.createDatabaseConfig(): DatabaseConfig {
    return DatabaseConfig(
        url = config.property("database.url").getString(),
        driver = config.property("database.driver").getString(),
        user = config.property("database.user").getString(),
        password = config.property("database.password").getString()
    )
}
