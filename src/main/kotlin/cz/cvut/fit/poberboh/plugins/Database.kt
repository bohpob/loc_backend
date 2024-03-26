package cz.cvut.fit.poberboh.plugins

import cz.cvut.fit.poberboh.database.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabase() {
    DatabaseSingleton.init()
}