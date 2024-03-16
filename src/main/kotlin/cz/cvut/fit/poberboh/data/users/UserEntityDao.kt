package cz.cvut.fit.poberboh.data.users

interface UserEntityDao {
    suspend fun readAllUsers(): List<UserEntity>
    suspend fun readUserById(id: Long): UserEntity?
    suspend fun readUserByUsername(username: String): UserEntity?
    suspend fun createUser(username: String, password: String, salt: String): UserEntity?
    suspend fun deleteUser(id: Long): Boolean
}