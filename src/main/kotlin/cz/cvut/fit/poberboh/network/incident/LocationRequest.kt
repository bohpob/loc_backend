package cz.cvut.fit.poberboh.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class LocationRequest(
    val incidentId: Long,
    val latitude: String,
    val longitude: String
)