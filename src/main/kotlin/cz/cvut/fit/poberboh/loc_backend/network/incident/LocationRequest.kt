package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

/**
 * Represents a record location request.
 *
 * @property incidentId The incident ID.
 * @property latitude The latitude.
 * @property longitude The longitude.
 */
@Serializable
data class LocationRequest(
    val incidentId: Long,
    val latitude: Double,
    val longitude: Double
)