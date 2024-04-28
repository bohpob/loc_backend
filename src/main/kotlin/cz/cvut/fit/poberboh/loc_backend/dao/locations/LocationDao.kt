package cz.cvut.fit.poberboh.loc_backend.dao.locations

import cz.cvut.fit.poberboh.loc_backend.models.Location

/**
 * Represents the location data access object.
 */
interface LocationDao {
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

    /**
     * Records a location.
     *
     * @param incidentId The incident ID.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @param timestamp The timestamp.
     * @return The location.
     */
    suspend fun recordLocation(incidentId: Long, latitude: Double, longitude: Double, timestamp: Long): Location?

    /**
     * Reads nearby incidents.
     *
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @return The list of locations.
     */
    suspend fun readNearbyIncidents(latitude: Double, longitude: Double): List<Location>
}