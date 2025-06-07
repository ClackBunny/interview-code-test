package com.hacker.utils.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * localDateTime的序列化器
 */
class LocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(formatter))
    }
}