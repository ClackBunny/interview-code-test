package com.hacker.models

import org.jetbrains.exposed.sql.Table

object TaskTable : Table("tasks") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val dueDate = varchar("due_date", 10)

    override val primaryKey = PrimaryKey(id)
}