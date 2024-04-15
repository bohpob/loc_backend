package cz.cvut.fit.poberboh.loc_backend.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?
)