package cz.cvut.fit.poberboh.loc_backend.security.token

data class TokenClaim(
    val name: String,
    val value: String
)