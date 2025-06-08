package com.hacker.services

import com.hacker.models.Task
import com.hacker.repository.TaskRepository


class TaskService(private val repository: TaskRepository) {
    fun getAllTasks() = repository.getAll()
    fun getTaskById(id: Int) = repository.getById(id)
    fun getTasksByDueDate(date: String) = repository.getByDueDate(date)
    fun addTask(task: Task) = repository.add(task)
    fun updateTask(id: Int, task: Task) = repository.update(id, task)
    fun deleteTask(id: Int) = repository.softDelete(id)
}