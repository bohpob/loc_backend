package cz.cvut.fit.poberboh.loc_backend.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * Service for creating JWT tokens. Implements [TokenService].
 */
class JwtTokenService : TokenService {
    /**
     * Creates an access token.
     *
     * @param config token configuration
     * @param claims token claims
     * @throws IllegalArgumentException if expiry time is negative
     * @return access token
     */
    override fun createAccessToken(config: TokenConfig, vararg claims: TokenClaim): String {
        if (config.expiresIn < 0) {
            throw IllegalArgumentException("Expiry time must be a non-negative value")
        }
        return generate(config, *claims, expiresIn = config.expiresIn)
    }

    /**
     * Creates a refresh token.
     *
     * @param config token configuration
     * @param claims token claims
     * @throws IllegalArgumentException if refresh time is negative
     * @return refresh token
     */
    override fun createRefreshToken(config: TokenConfig, vararg claims: TokenClaim): String {
        if (config.refreshIn < 0) {
            throw IllegalArgumentException("Refresh time must be a non-negative value")
        }
        return generate(config, *claims, expiresIn = config.refreshIn)
    }

    /**
     * Generates a token.
     *
     * @param config token configuration
     * @param claims token claims
     * @param expiresIn token expiration time in milliseconds
     * @return generated token
     */
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