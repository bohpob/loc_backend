package cz.cvut.fit.poberboh.loc_backend.network.incident

import cz.cvut.fit.poberboh.loc_backend.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.data.incidents.location.LocationDao
import cz.cvut.fit.poberboh.loc_backend.data.users.UserEntityDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Route.configureIncidentApi(userEntityDao: UserEntityDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    authenticate {
        createIncident(userEntityDao, incidentDao)
        stopShare(userEntityDao, incidentDao)
        recordLocation(userEntityDao, incidentDao, locationDao)
    }
}

fun Route.createIncident(userEntityDao: UserEntityDao, incidentDao: IncidentDao) {
    post {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
            val incident = call.receive<IncidentRequest>()

            val incidentResponse = incidentDao.createIncident(userId.toLong(), incident.category, incident.note)
            if (incidentResponse != null) {
                call.respond(
                    status = HttpStatusCode.Created,
                    incidentResponse
                )
            } else {
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "Failed to create incident"
                )
            }
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
        }
    }
}

fun Route.stopShare(userEntityDao: UserEntityDao, incidentDao: IncidentDao) {
    patch("{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
            val incidentId = call.parameters["id"]

            if (incidentId != null) {
                if (incidentDao.stopShare(incidentId.toLong())) {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = "Incident sharing stopped"
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = "Failed to stop sharing incident"
                    )
                }
            } else {
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "Failed to stop sharing incident"
                )
            }
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
        }
    }
}

fun Route.recordLocation(userEntityDao: UserEntityDao, incidentDao: IncidentDao, locationDao: LocationDao) {
    post("locations") {
        val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2))

        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class)

        if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
            val locationRequest = call.receive<LocationRequest>()

            if (incidentDao.readIncidentById(locationRequest.incidentId) != null) {
                val locationResponse = locationDao.recordLocation(
                    incidentId = locationRequest.incidentId,
                    latitude = locationRequest.latitude,
                    longitude = locationRequest.longitude,
                    timestamp
                )

                if (locationResponse != null) {
                    call.respond(
                        status = HttpStatusCode.Created,
                        "Location recorded"
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = "Failed to record location"
                    )
                }
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Incident not found"
                )
            }
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
        }
    }
}