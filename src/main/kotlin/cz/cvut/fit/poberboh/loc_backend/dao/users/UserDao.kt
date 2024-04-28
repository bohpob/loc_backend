package cz.cvut.fit.poberboh.loc_backend.dao.users

import cz.cvut.fit.poberboh.loc_backend.models.User

/**
 * Represents the user data access object.
 */
interface UserDao {
    /**
     * Reads all users.
     *
     * @return The list of users.
     */
    suspend fun readAll(): List<User>

    /**
     * Reads a user by ID.
     *
     * @param id The user ID.
     * @return The user.
     */
    suspend fun read(id: Long): User?

    /**
     * Reads a user by username.
     *
     * @param username The username.
     * @return The user.
     */
    suspend fun readByUsername(username: String): User?

    /**
     * Creates a user.
     *
     * @param username The username.
     * @param password The password.
     * @param salt The salt.
     * @return The user.
     */
    suspend fun create(username: String, password: String, salt: String): User?

    /**
     * Deletes a user by ID.
     *
     * @param id The user ID.
     * @return True if the user was deleted, false otherwise.
     */
    suspend fun delete(id: Long): Boolean
}