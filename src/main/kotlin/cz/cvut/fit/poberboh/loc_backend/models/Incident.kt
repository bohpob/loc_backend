package cz.cvut.fit.poberboh.loc_backend.models

import kotlinx.serialization.Serializable

@Serializable
data class Incident(
    val id: Long,
    val userId: Long,
    val category: String,
    val note: String?,
    val lastLocationId: Long?
)
