package cz.cvut.fit.poberboh.loc_backend.dao.archive.locations

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveLocations
import cz.cvut.fit.poberboh.loc_backend.models.Location
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class ArchiveLocationDaoImpl : ArchiveLocationDao {
    override suspend fun read(id: Long): Location? = dbQuery {
        ArchiveLocations.selectAll().where { ArchiveLocations.id eq id }
            .mapNotNull(::resultRowToArchiveLocation)
            .singleOrNull()
    }

    override suspend fun readAllByIncidentId(id: Long): List<Location> = dbQuery {
        ArchiveLocations.selectAll().where { ArchiveLocations.incidentId eq id }
            .map(::resultRowToArchiveLocation)
    }

    private fun resultRowToArchiveLocation(row: ResultRow) = Location(
        id = row[ArchiveLocations.id],
        incidentId = row[ArchiveLocations.incidentId],
        latitude = row[ArchiveLocations.latitude],
        longitude = row[ArchiveLocations.longitude],
        timestamp = row[ArchiveLocations.timestamp]
    )
}