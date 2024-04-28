package cz.cvut.fit.poberboh.loc_backend.security.hashing

/**
 * Service for hashing.
 */
interface HashingService {
    /**
     * Generates a salted hash.
     *
     * @param value value to hash
     * @param saltedLength length of the salt in bytes
     * @return salted hash
     */
    fun generateSaltedHash(value: String, saltedLength: Int = 32): SaltedHash

    /**
     * Verifies a value against a salted hash.
     *
     * @param value value to verify
     * @param saltedHash salted hash
     * @return true if the value matches the hash, false otherwise
     */
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}