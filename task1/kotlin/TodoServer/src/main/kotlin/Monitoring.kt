package com.hacker

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    var logger = LoggerFactory.getLogger("TodoServer")
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error("Unhandled exception caught: ${cause.message}", cause)
            call.respond(HttpStatusCode.InternalServerError, "Server error: ${cause.message}")
        }

        exception<IllegalArgumentException> { call, cause ->
            logger.warn("Bad request: ${cause.message}")
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Invalid input")
        }
    }
    install(CallLogging) {
        level = Level.INFO
//        filter { call -> call.request.path().startsWith("/") }
    }
}
