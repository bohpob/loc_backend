package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Long,
    val incidentId: Long,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
