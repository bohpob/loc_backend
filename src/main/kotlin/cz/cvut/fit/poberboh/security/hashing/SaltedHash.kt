package cz.cvut.fit.poberboh.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
