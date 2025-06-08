package com.hacker.utils.validator


import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator

object ValidatorUtil {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun <T : Any> validate(obj: T): List<String> {
        val violations: Set<ConstraintViolation<T>> = validator.validate(obj)
        return violations.map { "${it.propertyPath}: ${it.message}" }
    }
}