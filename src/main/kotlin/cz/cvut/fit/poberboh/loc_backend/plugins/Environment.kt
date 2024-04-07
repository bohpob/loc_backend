package cz.cvut.fit.poberboh.loc_backend.plugins

import cz.cvut.fit.poberboh.loc_backend.security.token.TokenConfig
import io.ktor.server.application.*

fun ApplicationEnvironment.createTokenConfig(): TokenConfig {
    return TokenConfig(
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        expiresIn = config.property("jwt.expiresIn").getString().toLong(),
        secret = config.property("jwt.secret").getString()
    )
}