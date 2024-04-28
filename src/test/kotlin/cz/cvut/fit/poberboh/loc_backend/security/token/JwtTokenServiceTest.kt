package cz.cvut.fit.poberboh.loc_backend.security.token

import org.junit.Assert.assertThrows
import kotlin.test.Test

class JwtTokenServiceTest {
    @Test
    fun testCreateAccessToken() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "secret")
        val token = tokenService.createAccessToken(config, TokenClaim("role", "admin"))

        assert(token.isNotEmpty())
    }

    @Test
    fun testCreateRefreshToken() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "secret")
        val token = tokenService.createRefreshToken(config, TokenClaim("user_id", "123456"))

        assert(token.isNotEmpty())
    }

    @Test
    fun testCreateAccessTokenWithClaims() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "secret")
        val token = tokenService.createAccessToken(
            config,
            TokenClaim("role", "admin"),
            TokenClaim("user_id", "123456")
        )

        assert(token.isNotEmpty())
    }

    @Test
    fun testCreateAccessTokenWithEmptyClaims() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "secret")
        val token = tokenService.createAccessToken(config)

        assert(token.isNotEmpty())
    }

    @Test
    fun testCreateAccessTokenWithNegativeExpiry() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", -3600, 1800, "secret")

        assertThrows(IllegalArgumentException::class.java) {
            tokenService.createAccessToken(config, TokenClaim("role", "admin"))
        }
    }

    @Test
    fun testCreateRefreshTokenWithNegativeExpiry() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, -1800, "secret")

        assertThrows(IllegalArgumentException::class.java) {
            tokenService.createRefreshToken(config, TokenClaim("user_id", "123456"))
        }
    }

    @Test
    fun testCreateAccessTokenWithEmptyConfig() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("", "", 0, 0, "")

        assertThrows(IllegalArgumentException::class.java) {
            tokenService.createAccessToken(config, TokenClaim("role", "admin"))
        }
    }

    @Test
    fun testCreateRefreshTokenWithEmptyClaims() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "secret")

        val token = tokenService.createRefreshToken(config)

        assert(token.isNotEmpty())
    }

    @Test
    fun testCreateAccessTokenWithInvalidSecret() {
        val tokenService = JwtTokenService()
        val config = TokenConfig("issuer", "audience", 3600, 1800, "")

        assertThrows(IllegalArgumentException::class.java) {
            tokenService.createAccessToken(config, TokenClaim("role", "admin"))
        }
    }
}