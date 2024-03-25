package cz.cvut.fit.poberboh.network.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val username: String?
)