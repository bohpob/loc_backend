package cz.cvut.fit.poberboh.loc_backend.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Represents the users table.
 */
object Locations : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val incidentId: Column<Long> = reference("incident_id", Incidents.id)
    val latitude: Column<Double> = double("latitude")
    val longitude: Column<Double> = double("longitude")
    val timestamp: Column<Long> = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}