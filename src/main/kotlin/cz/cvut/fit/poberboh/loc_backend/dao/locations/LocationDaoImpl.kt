package cz.cvut.fit.poberboh.loc_backend.dao.locations

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Incidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.Locations
import cz.cvut.fit.poberboh.loc_backend.models.Location
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Implementation of the [LocationDao] interface.
 */
class LocationDaoImpl : LocationDao {
    // Maximum distance in kilometers for locations to be considered near each other.
    private val maxDistance = 1.0

    /**
     * Reads all locations.
     *
     * @return a list of all locations.
     */
    override suspend fun readAllByIncidentId(id: Long): List<Location> = dbQuery {
        Locations.selectAll().where { Locations.incidentId eq id }
            .map(::resultRowToLocation)
    }

    /**
     * Reads a location by ID.
     *
     * @param id the ID of the location.
     * @return the location.
     */
    override suspend fun read(id: Long): Location? = dbQuery {
        Locations.selectAll().where { Locations.id eq id }
            .mapNotNull(::resultRowToLocation)
            .singleOrNull()
    }

    /**
     * Records a location.
     *
     * @param incidentId the incident ID.
     * @param latitude the latitude.
     * @param longitude the longitude.
     * @param timestamp the timestamp.
     * @return the location.
     */
    override suspend fun recordLocation(
        incidentId: Long,
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ): Location? = dbQuery {
        val newGPSIncident = Locations.insert {
            it[Locations.incidentId] = incidentId
            it[Locations.latitude] = latitude
            it[Locations.longitude] = longitude
            it[Locations.timestamp] = timestamp
        }
        newGPSIncident.resultedValues?.singleOrNull()?.let(::resultRowToLocation)
    }

    /**
     * Reads nearby incidents.
     *
     * @param latitude the latitude.
     * @param longitude the longitude.
     * @return the list of locations.
     */
    override suspend fun readNearbyIncidents(latitude: Double, longitude: Double): List<Location> = dbQuery {
        val lastLocationIds = Incidents.select(Incidents.lastLocationId)
            .where { Incidents.lastLocationId.isNotNull() }
            .map { it[Incidents.lastLocationId]!! }

        val locations = Locations.selectAll().where { Locations.id inList lastLocationIds }
            .map(::resultRowToLocation)

        locations.filter { calculateDistance(latitude, longitude, it.latitude, it.longitude) < maxDistance }
    }

    /**
     * Calculates the distance between two points.
     * Haversine formula.
     *
     * @param latitude1 the latitude of the first point.
     * @param longitude1 the longitude of the first point.
     * @param latitude2 the latitude of the second point.
     * @param longitude2 the longitude of the second point.
     * @return the distance between the two points.
     */
    private fun calculateDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Double {
        val earthRadius = 6371
        val latDistanceRadians = Math.toRadians(latitude2 - latitude1)
        val lonDistanceRadians = Math.toRadians(longitude2 - longitude1)
        val a = sin(latDistanceRadians / 2) * sin(latDistanceRadians / 2) +
                cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2)) *
                sin(lonDistanceRadians / 2) * sin(lonDistanceRadians / 2)
        val centralAngle = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * centralAngle
    }

    /**
     * Converts a result row to a location.
     *
     * @param row the result row.
     * @return the location.
     */
    private fun resultRowToLocation(row: ResultRow) = Location(
        id = row[Locations.id],
        incidentId = row[Locations.incidentId],
        latitude = row[Locations.latitude],
        longitude = row[Locations.longitude],
        timestamp = row[Locations.timestamp]
    )
}