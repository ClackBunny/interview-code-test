package com.hacker.config

import com.hacker.auth.JwtConfig
import com.hacker.models.UsersTable
import com.hacker.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            validate { credentials ->
                val username = credentials.payload.getClaim("username").asString()
                val tokenPasswordVersion = credentials.payload.getClaim("passwordVersion").asInt()

                val userRow = UserRepository.findUserByUsername(username)
                val dbPasswordVersion = userRow?.get(UsersTable.passwordVersion)
                if (dbPasswordVersion != null && dbPasswordVersion == tokenPasswordVersion) {
                    JWTPrincipal(credentials.payload)
                } else {
                    null // 拒绝访问
                }
            }
        }
    }
}
