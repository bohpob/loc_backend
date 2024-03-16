package cz.cvut.fit.poberboh.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object GPSIncidents : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val incidentId: Column<Long> = reference("incident_id", Incidents.id)
    val gpsX: Column<String> = varchar("gps_x", 255)
    val gpsY: Column<String> = varchar("gps_y", 255)
    val timestamp: Column<Long> = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}