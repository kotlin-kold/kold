package com.github.kold.validated

import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string

fun <T> validArb(value: Arb<T>): Arb<Validated.Valid<T>> = value.map { Validated.Valid(it) }

fun <T> invalidArb(): Arb<Validated.Invalid<T>> = Arb.list(violationArb(), 1..3).map { Validated.Invalid<T>(it) }

fun <T> validFieldArb(value: Arb<T>): Arb<ValidField<T>> =
    Arb.string().flatMap { fieldName ->
        value.map { it.validField(fieldName) }
    }

fun <T> invalidFieldArb(): Arb<InvalidField<T>> =
    Arb.bind(
        Arb.string(),
        invalidArb<T>()
    ) { fieldName, invalid -> invalid.violations.invalidField<T>(fieldName) }
