package com.hacker

import com.hacker.routes.registerTaskRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // 引入任务路由模块
        authenticate("auth") {
            registerTaskRoutes()
        }
    }
}
