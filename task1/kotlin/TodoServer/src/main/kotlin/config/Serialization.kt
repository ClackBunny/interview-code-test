package com.hacker.config

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.hacker.utils.serializer.LocalDateTimeSerializer
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import java.time.LocalDateTime

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            val module = SimpleModule().addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
            registerModule(JavaTimeModule())
            registerModule(module)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
        }
    }
}
