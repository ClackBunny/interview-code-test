package com.hacker.routes

import com.hacker.models.response.TokenResponse
import com.hacker.services.AuthService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerAuthRoutes() {
    route("/auth") {
        // 登录接口，不需要认证
        post("/login") {
            // 从请求体中读取用户名密码
            val credentials = call.receive<Map<String, String>>()
            val token = AuthService.login(
                credentials["username"]!!,
                credentials["password"]!!
            )
            if (token != null) {
                call.respond(HttpStatusCode.OK, TokenResponse(token = token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        // 修改密码接口，需要 JWT 认证
        authenticate {
            post("/change-password") {
                val data = call.receive<Map<String, String>>() // 接收旧密码、新密码
                val principal = call.principal<JWTPrincipal>()!! // 获取认证后的用户信息
                val username = principal.payload.getClaim("username").asString() // 提取用户名

                val success = AuthService.changePassword(
                    username,
                    data["oldPassword"]!!,
                    data["newPassword"]!!
                )

                if (success) {
                    call.respond(HttpStatusCode.OK, "Password changed")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Old password incorrect")
                }
            }
        }
    }
}
