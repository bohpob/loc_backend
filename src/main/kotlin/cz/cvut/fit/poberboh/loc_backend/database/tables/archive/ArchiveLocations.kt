package cz.cvut.fit.poberboh.loc_backend.database.tables.archive

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object ArchiveLocations : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val incidentId: Column<Long> = reference("incident_id", ArchiveIncidents.id)
    val latitude: Column<Double> = double("latitude")
    val longitude: Column<Double> = double("longitude")
    val timestamp: Column<Long> = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}