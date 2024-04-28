package cz.cvut.fit.poberboh.loc_backend.security.token

/**
 * Claim of a JWT token.
 *
 * @property name claim name
 * @property value claim value
 */
data class TokenClaim(
    val name: String,
    val value: String
)