package cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents

import cz.cvut.fit.poberboh.loc_backend.models.Incident

interface ArchiveIncidentDao {
    suspend fun readAll(): List<Incident>
    suspend fun readAllByUserId(id: Long): List<Incident>
    suspend fun read(id: Long): Incident?
}