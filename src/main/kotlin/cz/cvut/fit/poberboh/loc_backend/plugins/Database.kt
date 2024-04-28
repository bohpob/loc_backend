package cz.cvut.fit.poberboh.loc_backend.plugins

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseConfig
import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton
import io.ktor.server.application.*

/**
 * Configures the database.
 *
 * @param databaseConfig The database configuration.
 */
fun Application.configureDatabase(databaseConfig: DatabaseConfig) {
    // Initialize the database singleton.
    DatabaseSingleton.init(databaseConfig)
}