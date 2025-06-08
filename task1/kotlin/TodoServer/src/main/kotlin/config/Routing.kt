package com.hacker.config

import com.hacker.routes.registerAuthRoutes
import com.hacker.routes.registerTaskRoutes
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("api.json"){
            openApi()
        }
        route("swagger"){
            swaggerUI("/api.json")
        }
        registerAuthRoutes()
        // 引入任务路由模块
        authenticate {
            registerTaskRoutes()
        }
    }
}
