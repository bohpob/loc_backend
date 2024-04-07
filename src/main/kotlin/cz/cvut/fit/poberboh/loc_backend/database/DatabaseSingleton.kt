package cz.cvut.fit.poberboh.loc_backend.database

import cz.cvut.fit.poberboh.loc_backend.database.tables.Incidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.Locations
import cz.cvut.fit.poberboh.loc_backend.database.tables.UserEntities
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        val databaseName = "loc_db" // need to hide
        val user = "postgres"       // need to hide
        val password = "postgres"   // need to hide
        val host = "localhost"      //
        val port = "5432"           //

        val database = Database.connect(
            url = "jdbc:postgresql://$host:$port/$databaseName",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction(database) {
            SchemaUtils.create(UserEntities)
            SchemaUtils.create(Incidents)
            SchemaUtils.create(Locations)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}