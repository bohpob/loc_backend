package cz.cvut.fit.poberboh.loc_backend.plugins

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabase() {
    DatabaseSingleton.init()
}