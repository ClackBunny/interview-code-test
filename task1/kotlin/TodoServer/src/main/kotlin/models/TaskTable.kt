package com.hacker.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TaskTable : Table("tasks") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val dueDate = varchar("due_date", 10)
    val completed = bool("completed").default(false)

    val createTime = datetime("create_time")
    val lastUpdateTime = datetime("last_update_time")

    val deletedFlag = bool("delete_flag").default(false)
    override val primaryKey = PrimaryKey(id)
}