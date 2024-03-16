package cz.cvut.fit.poberboh.plugins

import cz.cvut.fit.poberboh.auth.authenticate
import cz.cvut.fit.poberboh.auth.login
import cz.cvut.fit.poberboh.auth.register
import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.security.hashing.HashingService
import cz.cvut.fit.poberboh.security.token.TokenConfig
import cz.cvut.fit.poberboh.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userEntityDao: UserEntityDao,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        login(hashingService, userEntityDao, tokenService, tokenConfig)
        register(hashingService, userEntityDao)
        authenticate()
    }
}
