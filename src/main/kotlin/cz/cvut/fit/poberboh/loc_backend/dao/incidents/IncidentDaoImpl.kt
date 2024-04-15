package cz.cvut.fit.poberboh.loc_backend.dao.incidents

import cz.cvut.fit.poberboh.loc_backend.models.Location
import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Incidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.Locations
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveIncidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveLocations
import cz.cvut.fit.poberboh.loc_backend.models.Incident
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class IncidentDaoImpl : IncidentDao {
    override suspend fun readAll(): List<Incident> = dbQuery {
        Incidents.selectAll().map(::resultRowToIncident)
    }

    override suspend fun readAllByUserId(id: Long): List<Incident> = dbQuery {
        Incidents.selectAll().where { Incidents.userId eq id }
            .map(::resultRowToIncident)
    }

    override suspend fun read(id: Long): Incident? = dbQuery {
        Incidents.selectAll().where { Incidents.id eq id }
            .mapNotNull(::resultRowToIncident)
            .singleOrNull()
    }

    override suspend fun stopShare(incident: Incident, locations: List<Location>): Boolean = dbQuery {
        ArchiveIncidents.insert {
            it[id] = incident.id
            it[userId] = incident.userId
            it[category] = incident.category
            it[note] = incident.note
            it[lastLocationId] = incident.lastLocationId
        }

        locations.forEach { location ->
            ArchiveLocations.insert {
                it[id] = location.id
                it[incidentId] = location.incidentId
                it[latitude] = location.latitude
                it[longitude] = location.longitude
                it[timestamp] = location.timestamp
            }
        }

        Locations.deleteWhere { incidentId eq incident.id } == locations.size
                && Incidents.deleteWhere { id eq incident.id } == 1
    }

    override suspend fun create(userId: Long, category: String, note: String?): Incident? =
        dbQuery {
            val newIncident = Incidents.insert {
                it[Incidents.userId] = userId
                it[Incidents.category] = category
                it[Incidents.note] = note
            }
            newIncident.resultedValues?.singleOrNull()?.let(::resultRowToIncident)
        }

    override suspend fun updateLastLocation(lastLocationId: Long, incidentId: Long): Boolean = dbQuery {
        Incidents.update({ Incidents.id eq incidentId }) { it[Incidents.lastLocationId] = lastLocationId } > 0
    }

    private fun resultRowToIncident(row: ResultRow) = Incident(
        id = row[Incidents.id],
        userId = row[Incidents.userId],
        category = row[Incidents.category],
        note = row[Incidents.note],
        lastLocationId = row[Incidents.lastLocationId]
    )
}