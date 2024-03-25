package cz.cvut.fit.poberboh.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class GPSIncidentRequest(
    val gpsX: String,
    val gpsY: String
)