package com.hacker.repository

import com.hacker.models.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    // 根据用户名查找用户记录
    fun findUserByUsername(username: String): ResultRow? = transaction {
        UsersTable.selectAll().where { UsersTable.username eq username }.singleOrNull()
    }

    // 创建用户
    fun createUser(username: String, hashedPassword: String): Int = transaction {
        UsersTable.insert {
            it[UsersTable.username] = username
            it[password] = hashedPassword
        } get UsersTable.id
    }

    // 修改密码
    fun updatePassword(username: String, hashedPassword: String): Boolean = transaction {
        UsersTable.update({ UsersTable.username eq username }) {
            it[password] = hashedPassword
            with(SqlExpressionBuilder) {
                it.update(passwordVersion, passwordVersion + 1)
            }
        } > 0
    }

    // 检查用户是否存在
    fun userExists(username: String): Boolean = transaction {
        UsersTable.selectAll().where { UsersTable.username eq username }.count() > 0
    }
}