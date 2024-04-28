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

/**
 * Represents a database singleton.
 */
object DatabaseSingleton {
    /**
     * Initializes the database.
     *
     * @param databaseConfig The database configuration.
     */
    fun init(databaseConfig: DatabaseConfig) {

        // Connect to the database.
        val database = Database.connect(
            url = databaseConfig.url,
            driver = databaseConfig.driver,
            user = databaseConfig.user,
            password = databaseConfig.password
        )

        // Create the tables.
        transaction(database) {
            SchemaUtils.create(Users, Incidents, Locations, ArchiveIncidents, ArchiveLocations)
        }
    }

    /**
     * Executes a database query.
     *
     * @param block The block.
     * @return The result.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}