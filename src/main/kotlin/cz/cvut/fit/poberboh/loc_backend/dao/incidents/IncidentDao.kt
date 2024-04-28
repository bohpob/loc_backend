package cz.cvut.fit.poberboh.loc_backend.dao.incidents

import cz.cvut.fit.poberboh.loc_backend.models.Location
import cz.cvut.fit.poberboh.loc_backend.models.Incident

/**
 * Represents the incident data access object.
 */
interface IncidentDao {
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

    /**
     * Creates an incident.
     *
     * @param userId The user ID.
     * @param category The category.
     * @param note The note.
     * @return The incident.
     */
    suspend fun create(userId: Long, category: String, note: String?): Incident?

    /**
     * Updates the last location.
     *
     * @param lastLocationId The last location ID.
     * @param incidentId The incident ID.
     * @return True if the last location was updated, false otherwise.
     */
    suspend fun updateLastLocation(lastLocationId: Long, incidentId: Long): Boolean

    /**
     * Starts sharing the incident.
     *
     * @param incident The incident.
     * @return True if the incident was shared, false otherwise.
     */
    suspend fun stopShare(incident: Incident, locations: List<Location>): Boolean
}