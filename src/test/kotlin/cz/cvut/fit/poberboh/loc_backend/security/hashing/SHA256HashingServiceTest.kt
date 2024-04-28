package cz.cvut.fit.poberboh.loc_backend.security.hashing

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SHA256HashingServiceTest {
    @Test
    fun testGenerateSaltedHash() {
        val hashingService = SHA256HashingService()
        val saltedHash = hashingService.generateSaltedHash("password", 32)

        assert(saltedHash.salt.isNotEmpty())
        assert(saltedHash.hash.isNotEmpty())
    }

    @Test
    fun testVerify() {
        val hashingService = SHA256HashingService()
        val saltedHash = hashingService.generateSaltedHash("password", 32)

        assert(hashingService.verify("password", saltedHash))
    }

    @Test
    fun testGenerateSaltedHashDifferentSalts() {
        val hashingService = SHA256HashingService()
        val saltedHash1 = hashingService.generateSaltedHash("password", 32)
        val saltedHash2 = hashingService.generateSaltedHash("password", 32)

        assert(saltedHash1.salt != saltedHash2.salt)
        assert(saltedHash1.hash != saltedHash2.hash)
    }

    @Test
    fun testVerifyIncorrectValue() {
        val hashingService = SHA256HashingService()
        val saltedHash = hashingService.generateSaltedHash("password", 32)

        assertFalse(hashingService.verify("incorrectPassword", saltedHash))
    }

    @Test
    fun testVerifyNullSalt() {
        val hashingService = SHA256HashingService()
        val saltedHash = SaltedHash("hash", "")

        assertFalse(hashingService.verify("password", saltedHash))
    }

    @Test
    fun testVerifyNullHash() {
        val hashingService = SHA256HashingService()
        val saltedHash = SaltedHash("", "salt")

        assertFalse(hashingService.verify("password", saltedHash))
    }

    @Test
    fun testGenerateSaltedHashEmptyString() {
        val hashingService = SHA256HashingService()
        val saltedHash = hashingService.generateSaltedHash("", 32)

        assert(saltedHash.salt.isNotEmpty())
        assert(saltedHash.hash.isNotEmpty())
    }

    @Test
    fun testVerifyEmptyString() {
        val hashingService = SHA256HashingService()
        val saltedHash = hashingService.generateSaltedHash("", 32)

        assertTrue(hashingService.verify("", saltedHash))
        assertFalse(hashingService.verify("password", saltedHash))
    }

    @Test
    fun testGenerateSaltedHashLongValue() {
        val hashingService = SHA256HashingService()
        val longValue = buildString {
            repeat(1000) { append("a") }
        }
        val saltedHash = hashingService.generateSaltedHash(longValue, 32)

        assert(saltedHash.salt.isNotEmpty())
        assert(saltedHash.hash.isNotEmpty())
    }

    @Test
    fun testVerifyLongValue() {
        val hashingService = SHA256HashingService()
        val longValue = buildString {
            repeat(1000) { append("a") }
        }
        val saltedHash = hashingService.generateSaltedHash(longValue, 32)

        assertTrue(hashingService.verify(longValue, saltedHash))
        assertFalse(hashingService.verify("password", saltedHash))
    }
}