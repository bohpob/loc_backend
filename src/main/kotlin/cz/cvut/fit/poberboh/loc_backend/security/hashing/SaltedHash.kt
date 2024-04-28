package cz.cvut.fit.poberboh.loc_backend.security.hashing

/**
 * Salted hash.
 *
 * @property hash hash
 * @property salt salt
 */
data class SaltedHash(
    val hash: String,
    val salt: String
)
