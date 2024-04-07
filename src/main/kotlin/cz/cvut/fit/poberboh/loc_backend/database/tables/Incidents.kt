package cz.cvut.fit.poberboh.loc_backend.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Incidents : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val userId: Column<Long> = reference("user_id", UserEntities.id)
    val category: Column<String> = varchar("category", 50)
    val state: Column<Boolean> = bool("state")
    val note: Column<String?> = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}