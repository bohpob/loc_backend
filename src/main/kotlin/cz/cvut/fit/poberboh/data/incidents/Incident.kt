package cz.cvut.fit.poberboh.data.incidents

import cz.cvut.fit.poberboh.data.State
import kotlinx.serialization.Serializable

@Serializable
data class Incident(
    val id: Long,
    val userEntityId: Long,
    val category: String,
    val state: State,
    val note: String?
)
