package cz.cvut.fit.poberboh.loc_backend.network.incident

import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.location.LocationDao
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

fun Route.configureIncidentApi() {
    val userDao = DaoProvider.provideUserEntityDao()
    val incidentDao = DaoProvider.provideIncidentDao()
    val locationDao = DaoProvider.provideLocationDao()

    authenticate {
        createIncident(userDao, incidentDao)
        stopShare(userDao, incidentDao, locationDao)
        recordLocation(userDao, incidentDao, locationDao)
    }

    readNearestLocations(locationDao)
}

fun Route.readNearestLocations(locationDao: LocationDao) {
    get {
        val request = call.receive<LocationRequest>()

        if (request.latitude == null || request.longitude == null) {
            call.respond(HttpStatusCode.BadRequest, "Latitude and longitude must be provided")
            return@get
        }

        val locations = locationDao.readNearestLocations(
            latitude = request.latitude,
            longitude = request.longitude
        )
        call.respond(HttpStatusCode.OK, locations)
    }
}

fun Route.createIncident(userDao: UserDao, incidentDao: IncidentDao) {
    post {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.readById(userId.toLong()) != null) {
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

fun Route.stopShare(userDao: UserDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    patch("{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.readById(userId.toLong()) != null) {
            val incidentId = call.parameters["id"]

            val incident = incidentId?.toLong()?.let { incidentDao.readById(it) }
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

fun Route.recordLocation(userDao: UserDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    post("locations") {
        val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2))

        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userDao.readById(userId.toLong()) != null) {
            val request = call.receive<RecordLocationRequest>()
            val incident = incidentDao.readById(request.incidentId)

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
