package cz.cvut.fit.poberboh.loc_backend.network.auth

import kotlinx.serialization.Serializable

/**
 * Represents a refresh token.
 *
 * @property refreshToken The refresh token.
 */
@Serializable
data class RefreshToken(
    val refreshToken: String?
)
