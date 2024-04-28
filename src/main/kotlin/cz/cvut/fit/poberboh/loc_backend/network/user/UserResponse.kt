package cz.cvut.fit.poberboh.loc_backend.network.user

import kotlinx.serialization.Serializable

/**
 * Represents a user response.
 *
 * @property username The username.
 */
@Serializable
data class UserResponse(
    val username: String?
)