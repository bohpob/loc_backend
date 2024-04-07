package cz.cvut.fit.poberboh.loc_backend.data.incidents

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Incidents
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class IncidentDaoImpl : IncidentDao {
    override suspend fun readAllIncidents(): List<Incident> = dbQuery {
        Incidents.selectAll().map(::resultRowToIncident)
    }

    override suspend fun readAllIncidentsByUserId(id: Long): List<Incident> = dbQuery {
        Incidents.selectAll().where { Incidents.userId eq id }
            .map(::resultRowToIncident)
    }

    override suspend fun readIncidentById(id: Long): Incident? = dbQuery {
        Incidents.selectAll().where { Incidents.id eq id }
            .map(::resultRowToIncident)
            .singleOrNull()
    }

    override suspend fun readState(id: Long): Boolean? = dbQuery {
        val incidentState: Boolean? = Incidents.selectAll()
            .where { Incidents.id eq id }
            .mapNotNull { it[Incidents.state] }
            .singleOrNull()
        incidentState
    }

    override suspend fun stopShare(id: Long): Boolean = dbQuery {
        val incident = Incidents.selectAll().where { Incidents.id eq id }.singleOrNull()
        if (incident != null) {
            Incidents.update({ Incidents.id eq id }) { it[state] = false }
            true
        } else {
            false
        }
    }

    override suspend fun createIncident(userId: Long, category: String, note: String?): Incident? =
        dbQuery {
            val newIncident = Incidents.insert {
                it[Incidents.userId] = userId
                it[Incidents.category] = category
                it[state] = true
                it[Incidents.note] = note
            }
            newIncident.resultedValues?.singleOrNull()?.let(::resultRowToIncident)
        }

    override suspend fun deleteIncident(id: Long): Boolean = dbQuery {
        Incidents.deleteWhere { Incidents.id eq id } > 0
    }

    private fun resultRowToIncident(row: ResultRow) = Incident(
        id = row[Incidents.id],
        userEntityId = row[Incidents.userId],
        category = row[Incidents.category],
        state = row[Incidents.state],
        note = row[Incidents.note]
    )
}