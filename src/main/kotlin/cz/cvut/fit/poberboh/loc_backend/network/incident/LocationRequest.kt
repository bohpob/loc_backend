package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class LocationRequest(
    val latitude: Double?,
    val longitude: Double?
)