package com.hacker.routes

import com.hacker.models.Task
import com.hacker.repository.TaskRepository
import com.hacker.services.TaskService
import com.hacker.utils.validator.receiveAndValidate
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktoropenapi.put
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerTaskRoutes() {
    val repository = TaskRepository()
    val service = TaskService(repository)
    route("/tasks") {
        get({
            description = "获取所有任务"
            tags("tasks")
        }) {
            call.respond(service.getAllTasks())
        }

        get("/{id}", {
            description="根据ID获取任务详情"
            tags("tasks")
            request {
                pathParameter<String>("id") {
                    description = "任务ID"
                    example("default") {
                        value = 1
                    }
                }
            }
            response {
                code(HttpStatusCode.OK) {
                    body<Task>()
                }
            }
        }) {
            val id = call.parameters["id"]?.toIntOrNull()
            val task = id?.let { service.getTaskById(it) }
            if (task != null) call.respond(task)
            else call.respond(HttpStatusCode.NotFound, "Task not found")
        }

        get("/due/{date}", {
            description = "截止日期的所有任务"
            tags("tasks")
            request {
                pathParameter<String>("date") {
                    description = "截止日期"
                    example("default") {
                        value = "2022-01-02"
                    }
                }
            }
            response {
                code(HttpStatusCode.OK) {
                    body<List<Task>>()
                }
            }
        }) {
            val date = call.parameters["date"]
            if (date != null) call.respond(service.getTasksByDueDate(date))
            else call.respond(HttpStatusCode.BadRequest, "Missing date")
        }

        post({
            description = "新增任务"
            tags("tasks")
            request {
                body<Task> {
                    required = true
                    example("default") {
                        value = Task(title = "default", description = "description")
                    }
                    example("completed") {
                        value = Task(title = "default", description = "description", completed = true)
                    }
                }
            }
            response {
                code(HttpStatusCode.OK) {
                    body<Boolean>()
                }
            }
        }) {
            val task = call.receiveAndValidate<Task>()
            call.respond(HttpStatusCode.Created, service.addTask(task))
        }

        put("/{id}", {
            description = "根据ID更新任务"
            tags("tasks")
            request {
                pathParameter<String>("id") { description = "任务ID" }
                body<Task>() {
                    required = true
                    example("completed") {
                        value = Task(
                            title = "default",
                            description = "description",
                            completed = true,
                            dueDate = "2022-01-02"
                        )
                    }
                }
            }
        }) {
            val id = call.parameters["id"]?.toIntOrNull()
            val task = call.receiveAndValidate<Task>()
            if (id != null && service.updateTask(id, task))
                call.respond(HttpStatusCode.OK, true)
            else
                call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}", {
            description = "根据ID删除任务"
            tags("tasks")
            request {
                pathParameter<String>("id") {
                    description = "任务ID"
                }
            }
        }) {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null && service.deleteTask(id))
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}
