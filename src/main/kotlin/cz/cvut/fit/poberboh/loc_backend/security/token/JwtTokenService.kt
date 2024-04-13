package cz.cvut.fit.poberboh.loc_backend.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService : TokenService {
    override fun createAccessToken(config: TokenConfig, vararg claims: TokenClaim): String {
        return generate(config, *claims, expiresIn = config.expiresIn)
    }

    override fun createRefreshToken(config: TokenConfig, vararg claims: TokenClaim): String {
        return generate(config, *claims, expiresIn = config.refreshIn)
    }

    private fun generate(config: TokenConfig, vararg claims: TokenClaim, expiresIn: Long): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}