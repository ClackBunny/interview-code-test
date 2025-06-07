package com.hacker.repository

import com.hacker.models.Task
import com.hacker.models.TaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class TaskRepository {

    fun getAll(): List<Task> = transaction {
        TaskTable.selectAll().map {
            it.toTask()
        }
    }

    fun getById(id: Int): Task? = transaction {
        TaskTable.selectAll().where { TaskTable.id eq id }.map { it.toTask() }.singleOrNull()
    }

    fun getByDueDate(date: String): List<Task> = transaction {
        TaskTable.selectAll().where { TaskTable.dueDate lessEq date }.map { it.toTask() }
    }

    fun add(task: Task): Task = transaction {
        val insertResult = TaskTable.insert {
            it[title] = task.title
            it[description] = task.description
            it[dueDate] = task.dueDate
        }

        // 获取自动生成的主键 ID
        val id = insertResult[TaskTable.id]

        // 返回带 ID 的 Task 对象
        task.copy(id = id)
    }

    fun update(id: Int, task: Task): Boolean = transaction {
        TaskTable.update({ TaskTable.id eq id }) {
            it[title] = task.title
            it[description] = task.description
            it[dueDate] = task.dueDate
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        TaskTable.deleteWhere { TaskTable.id eq id } > 0
    }

    private fun ResultRow.toTask() = Task(
        id = this[TaskTable.id],
        title = this[TaskTable.title],
        description = this[TaskTable.description],
        dueDate = this[TaskTable.dueDate]
    )
}
