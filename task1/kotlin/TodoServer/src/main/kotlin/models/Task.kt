package com.hacker.models

import java.time.LocalDateTime

data class Task(
    val id: Int? = null,
    val title: String,
    val description: String,
    val dueDate: String = "-1",
    val completed: Boolean = false,
    val createTime: LocalDateTime? = null,
    val lastUpdateTime: LocalDateTime? = null
)