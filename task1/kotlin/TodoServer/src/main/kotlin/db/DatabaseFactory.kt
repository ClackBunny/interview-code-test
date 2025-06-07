package com.hacker.db

import com.hacker.models.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(TaskTable)
        }
    }
}