package com.hacker.routes

import com.hacker.models.Task
import com.hacker.repository.TaskRepository
import com.hacker.services.TaskService
import com.hacker.utils.validator.receiveAndValidate
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerTaskRoutes() {
    val repository = TaskRepository()
    val service = TaskService(repository)
    route("/tasks") {
        get {
            call.respond(service.getAllTasks())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val task = id?.let { service.getTaskById(it) }
            if (task != null) call.respond(task)
            else call.respond(HttpStatusCode.NotFound, "Task not found")
        }

        get("/due/{date}") {
            val date = call.parameters["date"]
            if (date != null) call.respond(service.getTasksByDueDate(date))
            else call.respond(HttpStatusCode.BadRequest, "Missing date")
        }

        post {
            val task = call.receiveAndValidate<Task>()
            call.respond(HttpStatusCode.Created, service.addTask(task))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val task = call.receiveAndValidate<Task>()
            if (id != null && service.updateTask(id, task))
                call.respond(HttpStatusCode.OK, true)
            else
                call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null && service.deleteTask(id))
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}
