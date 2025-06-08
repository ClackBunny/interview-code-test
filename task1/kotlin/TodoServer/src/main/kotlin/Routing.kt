package com.hacker

import com.hacker.routes.registerAuthRoutes
import com.hacker.routes.registerTaskRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        registerAuthRoutes()
        // 引入任务路由模块
        authenticate {
            registerTaskRoutes()
        }
    }
}
