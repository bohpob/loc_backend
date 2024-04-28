package cz.cvut.fit.poberboh.loc_backend.security.token

/**
 * Configuration for token generation.
 *
 * @property issuer token issuer
 * @property audience token audience
 * @property expiresIn token expiration time in milliseconds
 * @property refreshIn token refresh time in milliseconds
 * @property secret token secret
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val refreshIn: Long,
    val secret: String
)
