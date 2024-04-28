package cz.cvut.fit.poberboh.loc_backend.network.auth

import kotlinx.serialization.Serializable

/**
 * Represents a token response.
 *
 * @property accessToken The access token.
 * @property refreshToken The refresh token.
 */
@Serializable
data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?
)