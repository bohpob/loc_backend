package cz.cvut.fit.poberboh.database

import cz.cvut.fit.poberboh.database.tables.Incidents
import cz.cvut.fit.poberboh.database.tables.GPSIncidents
import cz.cvut.fit.poberboh.database.tables.UserEntities
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        val databaseName: String = "loc_db" // need to hide
        val user: String = "postgres"       // need to hide
        val password: String = "postgres"   // need to hide

        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/$databaseName",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction(database) {
            SchemaUtils.create(UserEntities)
            SchemaUtils.create(Incidents)
            SchemaUtils.create(GPSIncidents)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}