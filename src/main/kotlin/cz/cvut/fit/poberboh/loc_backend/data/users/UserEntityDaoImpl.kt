package cz.cvut.fit.poberboh.loc_backend.data.users

import cz.cvut.fit.poberboh.loc_backend.database.DatabaseSingleton.dbQuery
import cz.cvut.fit.poberboh.loc_backend.database.tables.UserEntities
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserEntityDaoImpl : UserEntityDao {
    override suspend fun readAllUsers(): List<UserEntity> = dbQuery {
        UserEntities.selectAll().map(::resultRowToUserEntity)
    }

    override suspend fun readUserById(id: Long): UserEntity? = dbQuery {
        UserEntities.selectAll().where { UserEntities.id eq id }
            .map(::resultRowToUserEntity)
            .singleOrNull()
    }

    override suspend fun readUserByUsername(username: String): UserEntity? = dbQuery {
        UserEntities.selectAll().where { UserEntities.username eq username }
            .map(::resultRowToUserEntity)
            .singleOrNull()
    }

    override suspend fun createUser(username: String, password: String, salt: String): UserEntity? = dbQuery {
        val insertStatement = UserEntities.insert {
            it[UserEntities.username] = username
            it[UserEntities.password] = password
            it[UserEntities.salt] = salt
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUserEntity)
    }

    override suspend fun deleteUser(id: Long): Boolean = dbQuery {
        UserEntities.deleteWhere { UserEntities.id eq id } > 0
    }


    private fun resultRowToUserEntity(row: ResultRow) = UserEntity(
        id = row[UserEntities.id],
        username = row[UserEntities.username],
        password = row[UserEntities.password],
        salt = row[UserEntities.salt]
    )
}