package cz.cvut.fit.poberboh.loc_backend.network.incident

import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.dao.locations.LocationDao
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.di.DaoProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Configures the incident API.
 */
fun Route.configureIncidentApi() {
    // Initialize the DAOs.
    val userDao = DaoProvider.provideUserEntityDao()
    val incidentDao = DaoProvider.provideIncidentDao()
    val locationDao = DaoProvider.provideLocationDao()

    authenticate { // Routes that require authentication.
        createIncident(userDao, incidentDao)
        stopShare(userDao, incidentDao, locationDao)
        recordLocation(userDao, incidentDao, locationDao)
    }

    readNearbyIncidents(locationDao)
}

/**
 * Route. Reads nearby incidents.
 *
 * @param locationDao The location DAO.
 */
fun Route.readNearbyIncidents(locationDao: LocationDao) {
    get {
        val latitude = call.parameters["latitude"]?.toDoubleOrNull()
        val longitude = call.parameters["longitude"]?.toDoubleOrNull()

        if (latitude == null || longitude == null) {
            call.respond(HttpStatusCode.BadRequest, "Latitude and longitude must be provided")
            return@get
        }

        val locations = locationDao.readNearbyIncidents(
            latitude = latitude,
            longitude = longitude
        )

        val locationResponses = locations.map { location ->
            LocationResponse(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }

        call.respond(HttpStatusCode.OK, locationResponses)
    }
}

/**
 * Route. Creates an incident.
 *
 * @param userDao The user DAO.
 * @param incidentDao The incident DAO.
 */
fun Route.createIncident(userDao: UserDao, incidentDao: IncidentDao) {
    post {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.read(userId.toLong()) != null) {
            val incident = call.receive<IncidentRequest>()
            val incidentResponse = incidentDao.create(userId.toLong(), incident.category, incident.note)

            val statusCode =
                if (incidentResponse != null) HttpStatusCode.Created else HttpStatusCode.InternalServerError
            call.respond(status = statusCode, incidentResponse ?: "Failed to create incident")
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}

/**
 * Route. Stops sharing the location of the incident.
 *
 * @param userDao The user DAO.
 * @param incidentDao The incident DAO.
 * @param locationDao The location DAO.
 */
fun Route.stopShare(userDao: UserDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    delete("{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.read(userId.toLong()) != null) {
            val incidentId = call.parameters["id"]

            val incident = incidentId?.toLong()?.let { incidentDao.read(it) }
            val locations = incident?.id?.let { locationDao.readAllByIncidentId(it) }

            val success = incident != null && locations != null && incidentDao.stopShare(incident, locations)
            val statusCode = if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError

            call.respond(
                status = statusCode,
                if (success) "Incident sharing stopped" else "Failed to stop sharing incident"
            )
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}

/**
 * Route. Records a location.
 *
 * @param userDao The user DAO.
 * @param incidentDao The incident DAO.
 * @param locationDao The location DAO.
 */
fun Route.recordLocation(userDao: UserDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    post("locations") {
        val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2))

        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.read(userId.toLong()) != null) {
            val request = call.receive<LocationRequest>()
            val incident = incidentDao.read(request.incidentId)

            val statusCode = when {
                incident == null -> HttpStatusCode.NotFound to "Incident not found"
                else -> {
                    val locationResponse = locationDao.recordLocation(
                        incidentId = request.incidentId,
                        latitude = request.latitude,
                        longitude = request.longitude,
                        timestamp
                    )
                    val success = locationResponse != null && incidentDao.updateLastLocation(
                        locationResponse.id,
                        request.incidentId
                    )

                    if (success) HttpStatusCode.Created to "Location recorded"
                    else HttpStatusCode.InternalServerError to "Failed to record location"
                }
            }
            call.respond(status = statusCode.first, statusCode.second)
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}
