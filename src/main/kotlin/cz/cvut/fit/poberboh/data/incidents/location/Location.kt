package cz.cvut.fit.poberboh.data.incidents.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Long,
    val incidentId: Long,
    val latitude: String,
    val longitude: String,
    val timestamp: Long
)
