package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

/**
 * Represents a location response.
 *
 * @property latitude The latitude.
 * @property longitude The longitude.
 */
@Serializable
data class LocationResponse(
    val latitude: Double,
    val longitude: Double
)
