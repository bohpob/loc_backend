package cz.cvut.fit.poberboh.loc_backend.data.users

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: Long,
    val username: String,
    val password: String,
    val salt: String
)