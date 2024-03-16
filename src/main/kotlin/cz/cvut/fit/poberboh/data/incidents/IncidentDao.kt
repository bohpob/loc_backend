package cz.cvut.fit.poberboh.data.incidents

import cz.cvut.fit.poberboh.data.users.UserEntity

interface IncidentDao {
    suspend fun readAllIncidents(): List<Incident>
    suspend fun readAllIncidentsByUserId(): List<Incident>
    suspend fun readAllIncidentsByUserUsername(): List<Incident>
    suspend fun readIncident(id: Long): Incident?
    suspend fun createIncident(userEntity: UserEntity, category: String, note: String?): Incident?
    suspend fun deleteIncident(id: Long): Boolean
}