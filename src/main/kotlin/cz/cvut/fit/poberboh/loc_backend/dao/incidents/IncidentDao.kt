package cz.cvut.fit.poberboh.loc_backend.dao.incidents

import cz.cvut.fit.poberboh.loc_backend.models.Location
import cz.cvut.fit.poberboh.loc_backend.models.Incident

interface IncidentDao {
    suspend fun readAll(): List<Incident>
    suspend fun readAllByUserId(id: Long): List<Incident>
    suspend fun readById(id: Long): Incident?
    suspend fun create(userId: Long, category: String, note: String?): Incident?
    suspend fun updateLastLocation(lastLocationId: Long, incidentId: Long): Boolean
    suspend fun stopShare(incident: Incident, locations: List<Location>): Boolean
}