package cz.cvut.fit.poberboh.database.tables

import cz.cvut.fit.poberboh.data.State
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Incidents : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val userEntityId: Column<Long> = reference("user_entity_id", UserEntities.id)
    val category: Column<String> = varchar("category", 50)
    val state: Column<State> = enumerationByName("state", 20, State::class)
    val note: Column<String?> = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}