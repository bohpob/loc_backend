package cz.cvut.fit.poberboh.loc_backend.security.hashing

interface HashingService {
    fun generateSaltedHash(value: String, saltedLength: Int = 32): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}