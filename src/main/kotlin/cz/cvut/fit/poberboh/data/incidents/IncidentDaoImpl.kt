package cz.cvut.fit.poberboh.data.incidents

import cz.cvut.fit.poberboh.data.State
import cz.cvut.fit.poberboh.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.database.tables.Incidents
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class IncidentDaoImpl : IncidentDao {
    override suspend fun readAllIncidents(): List<Incident> = dbQuery {
        Incidents.selectAll().map(::resultRowToIncident)
    }

    override suspend fun readAllIncidentsByUserId(id: Long): List<Incident> = dbQuery {
        Incidents.selectAll().where { Incidents.userEntityId eq id }
            .map(::resultRowToIncident)
    }

    override suspend fun readIncidentById(id: Long): Incident? = dbQuery {
        Incidents.selectAll().where { Incidents.id eq id }
            .map(::resultRowToIncident)
            .singleOrNull()
    }

    override suspend fun readState(id: Long): State? = dbQuery {
        val incidentState: State? = Incidents.selectAll()
            .where { Incidents.id eq id }
            .mapNotNull { it[Incidents.state] }
            .singleOrNull()
        incidentState
    }

    override suspend fun switchState(id: Long): Boolean = dbQuery {
        val incident = Incidents.selectAll().where { Incidents.id eq id }.singleOrNull()

        if (incident != null) {
            val currentState = incident[Incidents.state]
            val newState = when (currentState) {
                State.Activated -> State.Deactivated
                State.Deactivated -> State.Activated
            }
            val rowsAffected = Incidents.update({ Incidents.id eq id }) {
                it[state] = newState
            }
            rowsAffected > 0
        } else {
            false
        }
    }

    override suspend fun createIncident(userId: Long, category: String, note: String?): Incident? =
        dbQuery {
            val newIncident = Incidents.insert {
                it[Incidents.userEntityId] = userId
                it[Incidents.category] = category
                it[Incidents.state] = State.Activated
                it[Incidents.note] = note
            }
            newIncident.resultedValues?.singleOrNull()?.let(::resultRowToIncident)
        }

    override suspend fun deleteIncident(id: Long): Boolean = dbQuery {
        Incidents.deleteWhere { Incidents.id eq id } > 0
    }

    private fun resultRowToIncident(row: ResultRow) = Incident(
        id = row[Incidents.id],
        userEntityId = row[Incidents.userEntityId],
        category = row[Incidents.category],
        state = row[Incidents.state],
        note = row[Incidents.note]
    )
}