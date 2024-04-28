package cz.cvut.fit.poberboh.loc_backend.dao.users

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Users
import cz.cvut.fit.poberboh.loc_backend.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

/**
 * Implementation of the [UserDao] interface.
 */
class UserDaoImpl : UserDao {
    /**
     * Reads all users.
     *
     * @return a list of all user.
     */
    override suspend fun readAll(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUserEntity)
    }

    /**
     * Reads a user by ID.
     *
     * @param id the ID of the user.
     * @return the user.
     */
    override suspend fun read(id: Long): User? = dbQuery {
        Users.selectAll().where { Users.id eq id }
            .mapNotNull(::resultRowToUserEntity)
            .singleOrNull()
    }

    /**
     * Reads a user by username.
     *
     * @param username the username of the user.
     * @return the user.
     */
    override suspend fun readByUsername(username: String): User? = dbQuery {
        Users.selectAll().where { Users.username eq username }
            .mapNotNull(::resultRowToUserEntity)
            .singleOrNull()
    }

    /**
     * Creates a user.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param salt the salt of the password.
     * @return the user.
     */
    override suspend fun create(username: String, password: String, salt: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.username] = username
            it[Users.password] = password
            it[Users.salt] = salt
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUserEntity)
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user.
     * @return true if the user was deleted, false otherwise.
     */
    override suspend fun delete(id: Long): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    /**
     * Converts a result row to a user.
     *
     * @param row the result row to convert.
     * @return the user.
     */
    private fun resultRowToUserEntity(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        salt = row[Users.salt]
    )
}