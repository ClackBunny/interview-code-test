package com.hacker.config

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.SchemaGenerator
import io.ktor.server.application.*

fun Application.configureOpenApi() {
    install(OpenApi) {
        schemas {
            generator = SchemaGenerator.reflection()
        }
    }
}