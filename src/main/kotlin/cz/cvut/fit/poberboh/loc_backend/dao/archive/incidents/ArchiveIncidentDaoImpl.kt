package cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveIncidents
import cz.cvut.fit.poberboh.loc_backend.models.Incident
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

/**
 * Implementation of the [ArchiveIncidentDao] interface.
 */
class ArchiveIncidentDaoImpl : ArchiveIncidentDao {
    /**
     * Reads all incidents.
     *
     * @return a list of all incidents.
     */
    override suspend fun readAll(): List<Incident> = dbQuery {
        ArchiveIncidents.selectAll().map(::resultRowToArchiveIncident)
    }

    /**
     * Reads all incidents by user ID.
     *
     * @param id the ID of the user.
     * @return a list of all incidents.
     */
    override suspend fun readAllByUserId(id: Long): List<Incident> = dbQuery {
        ArchiveIncidents.selectAll().where { ArchiveIncidents.userId eq id }
            .map(::resultRowToArchiveIncident)
    }

    /**
     * Reads an incident by ID.
     *
     * @param id the ID of the incident.
     * @return the incident.
     */
    override suspend fun read(id: Long): Incident? = dbQuery {
        ArchiveIncidents.selectAll().where { ArchiveIncidents.id eq id }
            .mapNotNull(::resultRowToArchiveIncident)
            .singleOrNull()
    }

    /**
     * Converts a result row to an incident.
     *
     * @param row The result row.
     * @return The incident.
     */
    private fun resultRowToArchiveIncident(row: ResultRow) = Incident(
        id = row[ArchiveIncidents.id],
        userId = row[ArchiveIncidents.userId],
        category = row[ArchiveIncidents.category],
        note = row[ArchiveIncidents.note],
        lastLocationId = row[ArchiveIncidents.lastLocationId]
    )
}