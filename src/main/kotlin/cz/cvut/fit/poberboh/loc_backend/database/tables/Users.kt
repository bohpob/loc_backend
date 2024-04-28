package cz.cvut.fit.poberboh.loc_backend.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Represents the users table.
 */
object Users : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val username: Column<String> = varchar("username", 255).uniqueIndex()
    val password: Column<String> = varchar("password", 255)
    val salt: Column<String> = varchar("salt", 255)

    override val primaryKey = PrimaryKey(id)
}