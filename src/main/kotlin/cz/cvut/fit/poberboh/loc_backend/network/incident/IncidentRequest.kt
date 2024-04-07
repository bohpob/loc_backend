package cz.cvut.fit.poberboh.loc_backend.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class IncidentRequest(
    val category: String,
    val note: String?
)