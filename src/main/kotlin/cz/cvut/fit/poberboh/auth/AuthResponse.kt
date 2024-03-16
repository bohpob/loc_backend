package cz.cvut.fit.poberboh.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
