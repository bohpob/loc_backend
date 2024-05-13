package cz.cvut.fit.poberboh.loc_backend.security.token

/**
 * Service for creating tokens.
 */
interface TokenService {
    /**
     * Creates an access token.
     *
     * @param config token configuration
     * @param claims token claims
     * @throws IllegalArgumentException if expiry time is negative
     * @return access token
     */
    fun createAccessToken(config: TokenConfig, vararg claims: TokenClaim): String

    /**
     * Creates a refresh token.
     *
     * @param config token configuration
     * @param claims token claims
     * @throws IllegalArgumentException if refresh time is negative
     * @return refresh token
     */
    fun createRefreshToken(config: TokenConfig, vararg claims: TokenClaim): String
}