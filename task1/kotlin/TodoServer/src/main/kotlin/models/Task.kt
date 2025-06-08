package com.hacker.models

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

data class Task(
    val id: Int? = null,

    @field:NotBlank(message = "标题不能为空")
    val title: String,

    @field:NotBlank(message = "描述不能为空")
    val description: String,

    @field:Pattern(
        regexp = "(-1)|([0-9]{4}-[0-9]{2}-[0-9]{2})",
        message = "dueDate 必须是 -1 或 yyyy-MM-dd 格式"
    )
    val dueDate: String = "-1",
    val completed: Boolean = false,
    val createTime: LocalDateTime? = null,
    val lastUpdateTime: LocalDateTime? = null
)