package cz.cvut.fit.poberboh.loc_backend.data.incidents.location

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Locations
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LocationDaoImpl : LocationDao {
    override suspend fun readAllLocationsByIncidentId(id: Long): List<Location> = dbQuery {
        Locations.selectAll().where { Locations.incidentId eq id }
            .map(::resultRowToGPSIncident)
    }

    override suspend fun readLastLocationByIncidentId(id: Long): Location? = dbQuery {
        Locations.selectAll().where { Locations.incidentId eq id }
            .orderBy(Locations.timestamp, SortOrder.DESC)
            .limit(1)
            .map(::resultRowToGPSIncident)
            .singleOrNull()
    }

    override suspend fun readLocation(id: Long): Location? = dbQuery {
        Locations.selectAll().where { Locations.id eq id }
            .map(::resultRowToGPSIncident)
            .singleOrNull()
    }

    override suspend fun recordLocation(
        incidentId: Long,
        latitude: String,
        longitude: String,
        timestamp: Long
    ): Location? = dbQuery {
        val newGPSIncident = Locations.insert {
            it[Locations.incidentId] = incidentId
            it[Locations.latitude] = latitude
            it[Locations.longitude] = longitude
            it[Locations.timestamp] = timestamp
        }
        newGPSIncident.resultedValues?.singleOrNull()?.let(::resultRowToGPSIncident)
    }

    override suspend fun deleteAllLocationsByIncidentId(id: Long): Boolean = dbQuery {
        Locations.deleteWhere { incidentId eq id } > 0
    }

    private fun resultRowToGPSIncident(row: ResultRow) = Location(
        id = row[Locations.id],
        incidentId = row[Locations.incidentId],
        latitude = row[Locations.latitude],
        longitude = row[Locations.longitude],
        timestamp = row[Locations.timestamp]
    )
}