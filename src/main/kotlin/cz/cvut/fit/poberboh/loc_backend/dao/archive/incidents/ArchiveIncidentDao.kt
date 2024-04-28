package cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents

import cz.cvut.fit.poberboh.loc_backend.models.Incident

/**
 * Represents the archive incident data access object.
 */
interface ArchiveIncidentDao {
    /**
     * Reads all incidents.
     *
     * @return The list of incidents.
     */
    suspend fun readAll(): List<Incident>

    /**
     * Reads all incidents by user ID.
     *
     * @param id The user ID.
     * @return The list of incidents.
     */
    suspend fun readAllByUserId(id: Long): List<Incident>

    /**
     * Reads an incident by ID.
     *
     * @param id The incident ID.
     * @return The incident.
     */
    suspend fun read(id: Long): Incident?
}