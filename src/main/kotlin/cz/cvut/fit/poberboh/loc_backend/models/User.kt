package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

/**
 * Represents a user.
 *
 * @property id The user ID.
 * @property username The username.
 * @property password The password.
 * @property salt The salt.
 */
@Serializable
data class User(
    val id: Long,
    val username: String,
    val password: String,
    val salt: String
)