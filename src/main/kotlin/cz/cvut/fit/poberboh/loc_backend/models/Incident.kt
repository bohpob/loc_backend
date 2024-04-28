package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

/**
 * Represents an incident.
 *
 * @property id The incident ID.
 * @property userId The user ID.
 * @property category The category.
 * @property note The note.
 * @property lastLocationId The last location ID.
 */
@Serializable
data class Incident(
    val id: Long,
    val userId: Long,
    val category: String,
    val note: String?,
    val lastLocationId: Long?
)
