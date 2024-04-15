package cz.cvut.fit.poberboh.loc_backend.dao.users

import cz.cvut.fit.poberboh.loc_backend.models.User

interface UserDao {
    suspend fun readAll(): List<User>
    suspend fun read(id: Long): User?
    suspend fun readByUsername(username: String): User?
    suspend fun create(username: String, password: String, salt: String): User?
    suspend fun delete(id: Long): Boolean
}