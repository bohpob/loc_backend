package cz.cvut.fit.poberboh.loc_backend.database

/**
 * Represents the database configuration.
 *
 * @property url The database URL.
 * @property driver The database driver.
 * @property user The database user.
 * @property password The database password.
 */
data class DatabaseConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)
