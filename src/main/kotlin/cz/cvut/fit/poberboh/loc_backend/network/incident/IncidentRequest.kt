package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

/**
 * Represents an incident request.
 *
 * @property category The category.
 * @property note The note.
 */
@Serializable
data class IncidentRequest(
    val category: String,
    val note: String?
)