package cz.cvut.fit.poberboh.data.incidents.gps

import cz.cvut.fit.poberboh.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.database.tables.GPSIncidents
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class GPSIncidentDaoImpl : GPSIncidentDao {
    override suspend fun readAllGPSIncidentsByIncidentId(id: Long): List<GPSIncident> = dbQuery {
        GPSIncidents.selectAll().where { GPSIncidents.incidentId eq id }
            .map(::resultRowToGPSIncident)
    }

    override suspend fun readLastGPSIncidentByIncidentId(id: Long): GPSIncident? = dbQuery {
        GPSIncidents.selectAll().where { GPSIncidents.incidentId eq id }
            .orderBy(GPSIncidents.timestamp, SortOrder.DESC)
            .limit(1)
            .map(::resultRowToGPSIncident)
            .singleOrNull()
    }

    override suspend fun readGPSIncident(id: Long): GPSIncident? = dbQuery {
        GPSIncidents.selectAll().where { GPSIncidents.id eq id }
            .map(::resultRowToGPSIncident)
            .singleOrNull()
    }

    override suspend fun createGPSIncident(
        incidentId: Long,
        gpsX: String,
        gpsY: String,
        timestamp: Long
    ): GPSIncident? = dbQuery {
        val newGPSIncident = GPSIncidents.insert {
            it[GPSIncidents.incidentId] = incidentId
            it[GPSIncidents.gpsX] = gpsX
            it[GPSIncidents.gpsY] = gpsY
            it[GPSIncidents.timestamp] = timestamp
        }
        newGPSIncident.resultedValues?.singleOrNull()?.let(::resultRowToGPSIncident)
    }

    override suspend fun deleteAllGPSIncidentsByIncidentId(id: Long): Boolean = dbQuery {
        GPSIncidents.deleteWhere { GPSIncidents.incidentId eq id } > 0
    }

    private fun resultRowToGPSIncident(row: ResultRow) = GPSIncident(
        id = row[GPSIncidents.id],
        incidentId = row[GPSIncidents.incidentId],
        gpsX = row[GPSIncidents.gpsX],
        gpsY = row[GPSIncidents.gpsY],
        timestamp = row[GPSIncidents.timestamp]
    )
}