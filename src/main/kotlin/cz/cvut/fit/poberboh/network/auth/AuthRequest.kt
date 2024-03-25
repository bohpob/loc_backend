package cz.cvut.fit.poberboh.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)