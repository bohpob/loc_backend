package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val username: String,
    val password: String,
    val salt: String
)