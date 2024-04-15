package cz.cvut.fit.poberboh.loc_backend.dao.users

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.Users
import cz.cvut.fit.poberboh.loc_backend.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserDaoImpl : UserDao {
    override suspend fun readAll(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUserEntity)
    }

    override suspend fun read(id: Long): User? = dbQuery {
        Users.selectAll().where { Users.id eq id }
            .mapNotNull(::resultRowToUserEntity)
            .singleOrNull()
    }

    override suspend fun readByUsername(username: String): User? = dbQuery {
        Users.selectAll().where { Users.username eq username }
            .mapNotNull(::resultRowToUserEntity)
            .singleOrNull()
    }

    override suspend fun create(username: String, password: String, salt: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.username] = username
            it[Users.password] = password
            it[Users.salt] = salt
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUserEntity)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    private fun resultRowToUserEntity(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        salt = row[Users.salt]
    )
}