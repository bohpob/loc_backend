package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val latitude: Double,
    val longitude: Double
)
