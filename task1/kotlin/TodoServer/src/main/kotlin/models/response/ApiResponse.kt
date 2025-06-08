package com.hacker.models.response

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
)
