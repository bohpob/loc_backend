package cz.cvut.fit.poberboh.loc_backend.database

data class DatabaseConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)
