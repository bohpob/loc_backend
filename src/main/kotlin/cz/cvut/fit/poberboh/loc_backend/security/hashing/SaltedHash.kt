package cz.cvut.fit.poberboh.loc_backend.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)