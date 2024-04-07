package cz.cvut.fit.poberboh.loc_backend.data.incidents

interface IncidentDao {
    suspend fun readAllIncidents(): List<Incident>
    suspend fun readAllIncidentsByUserId(id: Long): List<Incident>
    suspend fun readIncidentById(id: Long): Incident?
    suspend fun readState(id: Long): Boolean?
    suspend fun stopShare(id: Long): Boolean
    suspend fun createIncident(userId: Long, category: String, note: String?): Incident?
    suspend fun deleteIncident(id: Long): Boolean
}