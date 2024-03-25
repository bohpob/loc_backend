package cz.cvut.fit.poberboh.network.incident

import cz.cvut.fit.poberboh.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.data.incidents.gps.GPSIncidentDao
import cz.cvut.fit.poberboh.data.users.UserEntityDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Route.configureIncidentApi(userEntityDao: UserEntityDao, incidentDao: IncidentDao, gpsIncidentDao: GPSIncidentDao) {
    authenticate {
        post {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
                val incident = call.receive<IncidentRequest>()

                call.respond(
                    status = HttpStatusCode.OK,
                    incidentDao.createIncident(userId.toLong(), incident.category, incident.note)!! //@todo
                )
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "User not found"
                )
            }
        }

        patch("switch/{id?}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
                val incidentId = call.parameters["id"]
                if (incidentId != null && incidentDao.readIncidentById(incidentId.toLong()) != null) {
                    if (incidentDao.switchState(incidentId.toLong())) { //@todo
                        call.respond(status = HttpStatusCode.OK, message = "OK")
                    } else {
                        call.respond(status = HttpStatusCode.Conflict, message = "")
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

        post("{id?}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            if (userId != null && userEntityDao.readUserById(userId.toLong()) != null) {
                val gpsIncident = call.receive<GPSIncidentRequest>()
                val incidentId = call.parameters["id"]
                val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2)) // UTC + 2 @todo

                if (incidentId != null && incidentDao.readIncidentById(incidentId.toLong()) != null) {
                    call.respond(
                        status = HttpStatusCode.OK,
                        gpsIncidentDao.createGPSIncident(
                            incidentId.toLong(),
                            gpsIncident.gpsX,
                            gpsIncident.gpsY,
                            timestamp
                        )!! //@todo
                    )
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
}