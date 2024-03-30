package cz.cvut.fit.poberboh.data.incidents

import kotlinx.serialization.Serializable

@Serializable
data class Incident(
    val id: Long,
    val userEntityId: Long,
    val category: String,
    val state: Boolean,
    val note: String?
)
