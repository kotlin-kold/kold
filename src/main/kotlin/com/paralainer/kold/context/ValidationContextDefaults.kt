package com.paralainer.kold.context

import com.paralainer.kold.validated.ValueViolation

class ValidationContextDefaults(
    val nullValue: ValueViolation = ValueViolation(
        "value.not.null",
        "Value can't be null"
    ),
    val invalidValue: ValueViolation = ValueViolation(
        "value.invalid.format",
        "Value is invalid format"
    ),
    val invalidElements: ValueViolation = ValueViolation(
        "value.elements.invalid",
        "Elements of array are in invalid format"
    ),
    val intOverflow: ValueViolation = ValueViolation(
        "value.int.overflow",
        "Value overflows Int type"
    )
)
