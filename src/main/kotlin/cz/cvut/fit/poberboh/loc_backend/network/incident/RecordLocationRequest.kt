package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class RecordLocationRequest(
    val incidentId: Long,
    val latitude: Double,
    val longitude: Double
)