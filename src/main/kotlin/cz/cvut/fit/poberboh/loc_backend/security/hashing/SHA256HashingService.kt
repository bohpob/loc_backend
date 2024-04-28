package cz.cvut.fit.poberboh.loc_backend.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * Service for hashing using SHA-256. Implements [HashingService].
 */
class SHA256HashingService : HashingService {
    /**
     * Generates a salted hash.
     *
     * @param value value to hash
     * @param saltedLength length of the salt in bytes
     * @return salted hash
     */
    override fun generateSaltedHash(value: String, saltedLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltedLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(hash, saltAsHex)
    }

    /**
     * Verifies a value against a salted hash.
     *
     * @param value value to verify
     * @param saltedHash salted hash
     * @return true if the value matches the hash, false otherwise
     */
    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}