package cz.cvut.fit.poberboh.data.incidents.gps

import kotlinx.serialization.Serializable

@Serializable
data class GPSIncident(
    val id: Long,
    val incidentId: Long,
    val gpsX: String,
    val gpsY: String,
    val timestamp: Long
)
