package cz.cvut.fit.poberboh.loc_backend.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Represents the users table.
 */
object Incidents : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val userId: Column<Long> = reference("user_id", Users.id)
    val category: Column<String> = varchar("category", 50)
    val note: Column<String?> = text("note").nullable()
    val lastLocationId: Column<Long?> = long("last_location_id").nullable()

    override val primaryKey = PrimaryKey(id)
}