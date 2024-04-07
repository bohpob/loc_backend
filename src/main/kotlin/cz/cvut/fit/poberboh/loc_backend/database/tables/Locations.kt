package cz.cvut.fit.poberboh.loc_backend.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Locations : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val incidentId: Column<Long> = reference("incident_id", Incidents.id)
    val latitude: Column<String> = varchar("latitude", 255)
    val longitude: Column<String> = varchar("longitude", 255)
    val timestamp: Column<Long> = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}