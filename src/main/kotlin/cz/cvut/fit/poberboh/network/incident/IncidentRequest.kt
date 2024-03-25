package cz.cvut.fit.poberboh.network.incident

import kotlinx.serialization.Serializable

@Serializable
data class IncidentRequest(
    val category: String,
    val note: String?
)