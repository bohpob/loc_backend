package cz.cvut.fit.poberboh.loc_backend.security.token

interface TokenService {
    fun createAccessToken(config: TokenConfig, vararg claims: TokenClaim): String
    fun createRefreshToken(config: TokenConfig, vararg claims: TokenClaim): String
}