package com.hacker.db

import com.hacker.models.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {
    fun init() {
        val dbPath = "data/todo.db"
        ensureDatabaseDirExists(dbPath)
        Database.connect("jdbc:sqlite:$dbPath", driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(TaskTable)
        }
    }

    private fun ensureDatabaseDirExists(dbPath: String) {
        val dbFile = File(dbPath)
        val parentDir = dbFile.parentFile
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs() // 自动创建多级目录
        }
    }
}