package cz.cvut.fit.poberboh.data.incidents.location

interface LocationDao {
    suspend fun readAllLocationsByIncidentId(id: Long): List<Location>
    suspend fun readLastLocationByIncidentId(id: Long): Location?
    suspend fun readLocation(id: Long): Location?
    suspend fun recordLocation(incidentId: Long, latitude: String, longitude: String, timestamp: Long): Location?
    suspend fun deleteAllLocationsByIncidentId(id: Long): Boolean
}