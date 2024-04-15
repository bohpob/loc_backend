package cz.cvut.fit.poberboh.loc_backend.dao.locations

import cz.cvut.fit.poberboh.loc_backend.models.Location

interface LocationDao {
    suspend fun read(id: Long): Location?
    suspend fun readAllByIncidentId(id: Long): List<Location>
    suspend fun recordLocation(incidentId: Long, latitude: Double, longitude: Double, timestamp: Long): Location?
    suspend fun readNearestLocations(latitude: Double, longitude: Double): List<Location>
}