package cz.cvut.fit.poberboh.loc_backend.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cz.cvut.fit.poberboh.loc_backend.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * Configures security.
 *
 * @param config The token configuration.
 */
fun Application.configureSecurity(config: TokenConfig) {
    authentication {
        // Configure JWT authentication.
        jwt {
            this.realm = this@configureSecurity.environment.config.property("jwt.realm").getString()

            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )

            // Validate the credentials.
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
