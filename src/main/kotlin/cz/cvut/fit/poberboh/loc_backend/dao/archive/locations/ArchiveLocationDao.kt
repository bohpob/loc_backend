package cz.cvut.fit.poberboh.loc_backend.dao.archive.locations

import cz.cvut.fit.poberboh.loc_backend.models.Location

interface ArchiveLocationDao {
    suspend fun read(id: Long): Location?
    suspend fun readAllByIncidentId(id: Long): List<Location>
}