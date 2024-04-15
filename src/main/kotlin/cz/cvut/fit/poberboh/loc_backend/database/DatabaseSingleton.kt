package cz.cvut.fit.poberboh.loc_backend.database

import cz.cvut.fit.poberboh.loc_backend.database.tables.Incidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.Locations
import cz.cvut.fit.poberboh.loc_backend.database.tables.Users
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveIncidents
import cz.cvut.fit.poberboh.loc_backend.database.tables.archive.ArchiveLocations
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init(databaseConfig: DatabaseConfig) {

        val database = Database.connect(
            url = databaseConfig.url,
            driver = databaseConfig.driver,
            user = databaseConfig.user,
            password = databaseConfig.password
        )

        transaction(database) {
            SchemaUtils.create(Users, Incidents, Locations, ArchiveIncidents, ArchiveLocations)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}