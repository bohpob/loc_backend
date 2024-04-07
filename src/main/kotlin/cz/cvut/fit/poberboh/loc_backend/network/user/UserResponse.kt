package cz.cvut.fit.poberboh.loc_backend.network.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val username: String?
)