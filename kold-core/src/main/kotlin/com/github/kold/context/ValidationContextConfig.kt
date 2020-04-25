package com.github.kold.context

import com.github.kold.validated.ValueViolation

data class ValidationContextConfig(
    val nullValue: ValueViolation = ValueViolation(
        "value.not.null",
        "Value can't be null"
    ),
    val invalidValue: ValueViolation = ValueViolation(
        "value.invalid.format",
        "Value is invalid format"
    ),
    val intOverflow: ValueViolation = ValueViolation(
        "value.int.overflow",
        "Value overflows Int type"
    )
)
