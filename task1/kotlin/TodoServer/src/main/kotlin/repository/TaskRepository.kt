package com.hacker.repository

import com.hacker.models.Task
import com.hacker.models.TaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class TaskRepository {

    fun getAll(): List<Task> = transaction {
        TaskTable.selectAll().where { TaskTable.deletedFlag eq false }.map {
            it.toTask()
        }
    }

    fun getById(id: Int): Task? = transaction {
        TaskTable.selectAll().where { (TaskTable.id eq id) and (TaskTable.deletedFlag eq false) }.map { it.toTask() }
            .singleOrNull()
    }

    fun getByDueDate(date: String): List<Task> = transaction {
        TaskTable.selectAll().where { TaskTable.dueDate eq date and (TaskTable.deletedFlag eq false) }
            .map { it.toTask() }
    }

    fun add(task: Task): Task = transaction {
        val now = LocalDateTime.now()
        val insertResult = TaskTable.insert {
            it[title] = task.title
            it[description] = task.description
            it[dueDate] = task.dueDate
            it[completed] = task.completed
            it[createTime] = now
            it[lastUpdateTime] = now
            it[deletedFlag] = false
        }

        // 获取自动生成的主键 ID
        val id = insertResult[TaskTable.id]
        val created = insertResult[TaskTable.createTime]
        val updated = insertResult[TaskTable.lastUpdateTime]

        // 返回带 ID 的 Task 对象
        task.copy(
            id = id,
            createTime = created,
            lastUpdateTime = updated
        )
    }

    fun update(id: Int, task: Task): Boolean = transaction {
        TaskTable.update({ TaskTable.id eq id and (TaskTable.deletedFlag eq false) }) {
            if (task.title.isNotBlank()) it[title] = task.title
            it[description] = task.description
            it[dueDate] = task.dueDate
            it[completed] = task.completed
            it[lastUpdateTime] = LocalDateTime.now()
        } > 0
    }

    fun softDelete(id: Int): Boolean = transaction {
        TaskTable.update({ TaskTable.id eq id }) {
            it[deletedFlag] = true
            it[lastUpdateTime] = LocalDateTime.now()
        } > 0
    }

    /**
     * 数据库数据转换成前端数据
     */
    private fun ResultRow.toTask() = Task(
        id = this[TaskTable.id],
        title = this[TaskTable.title],
        description = this[TaskTable.description],
        dueDate = this[TaskTable.dueDate],
        completed = this[TaskTable.completed],
        createTime = this[TaskTable.createTime],
        lastUpdateTime = this[TaskTable.lastUpdateTime]
    )
}
