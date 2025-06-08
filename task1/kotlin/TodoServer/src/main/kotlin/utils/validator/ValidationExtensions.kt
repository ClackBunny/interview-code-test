package com.hacker.utils.validator

import io.ktor.server.application.*
import io.ktor.server.request.*

suspend inline fun <reified T : Any> ApplicationCall.receiveAndValidate(): T {
    val obj = receive<T>()
    val errors = ValidatorUtil.validate(obj)
    if (errors.isNotEmpty()) {
        throw ValidationException(errors)
    }
    return obj
}

class ValidationException(val errors: List<String>) : RuntimeException()
