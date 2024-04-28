package cz.cvut.fit.poberboh.loc_backend.network.auth

import kotlinx.serialization.Serializable

/**
 * Represents an authentication request.
 *
 * @property username The username.
 * @property password The password.
 */
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)