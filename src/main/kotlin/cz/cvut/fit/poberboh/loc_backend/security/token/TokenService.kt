package cz.cvut.fit.poberboh.loc_backend.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}