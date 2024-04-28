package cz.cvut.fit.poberboh.loc_backend.dao.archive.locations

import cz.cvut.fit.poberboh.loc_backend.models.Location

/**
 * Represents the archive location data access object.
 */
interface ArchiveLocationDao {
    /**
     * Reads a location by ID.
     *
     * @param id The location ID.
     * @return The location.
     */
    suspend fun read(id: Long): Location?

    /**
     * Reads all locations by incident ID.
     *
     * @param id The incident ID.
     * @return The list of locations.
     */
    suspend fun readAllByIncidentId(id: Long): List<Location>
}