package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

/**
 * Represents a location.
 *
 * @property id The location ID.
 * @property incidentId The incident ID.
 * @property latitude The latitude.
 * @property longitude The longitude.
 * @property timestamp The timestamp.
 */
@Serializable
data class Location(
    val id: Long,
    val incidentId: Long,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
