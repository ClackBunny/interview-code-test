package com.hacker.models

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 64) // 哈希后存储
    val passwordVersion = integer("password_version").default(1)
    override val primaryKey = PrimaryKey(id)
}
