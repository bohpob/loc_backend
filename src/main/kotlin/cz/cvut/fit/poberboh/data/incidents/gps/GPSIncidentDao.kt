package cz.cvut.fit.poberboh.data.incidents.gps

interface GPSIncidentDao {
    suspend fun readAllGPSIncidentsByIncidentId(id: Long): List<GPSIncident>
    suspend fun readLastGPSIncidentByIncidentId(id: Long): GPSIncident?
    suspend fun readGPSIncident(id: Long): GPSIncident?
    suspend fun deleteAllGPSIncidentsByIncidentId(id: Long): List<GPSIncident>
}