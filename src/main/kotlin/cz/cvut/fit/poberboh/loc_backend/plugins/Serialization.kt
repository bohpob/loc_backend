package cz.cvut.fit.poberboh.loc_backend.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Configures serialization.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json() // Use the JSON content type.
    }
}
