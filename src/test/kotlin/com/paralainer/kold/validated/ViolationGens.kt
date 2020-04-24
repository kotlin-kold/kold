package com.paralainer.kold.validated

import com.paralainer.kold.utils.orNull
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.choose
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import kotlin.math.max

val ValueViolationArb: Arb<ValueViolation> = Arb.bind(
    Arb.string(),
    Arb.string().orNull()
) { errorCode, description ->
    ValueViolation(errorCode, description)
}

val FlatViolationArb: Arb<FlatViolation> = Arb.bind(
    Arb.string().orNull(),
    Arb.string(),
    Arb.string().orNull()
) { fieldName, errorCode, description ->
    FlatViolation(fieldName, errorCode, description)
}

fun fieldViolation(maxDepth: Int = 3): Arb<FieldViolation> =
    Arb.bind(
        Arb.string(),
        Arb.list(violationArb(maxDepth - 1), 1..10)
    ) { fieldName, violations ->
        FieldViolation(fieldName, violations)
    }

fun elementsViolation(maxDepth: Int = 3): Arb<ElementsViolation> =
    Arb.list(violationArb(maxDepth - 1), 1..10).map {
        ElementsViolation(it)
    }

fun violationArb(maxDepth: Int = 2): Arb<Violation> =
    if (maxDepth > 0) {
        Arb.choice(
            ValueViolationArb,
            fieldViolation(maxDepth),
            elementsViolation(maxDepth)
        )
    } else {
        ValueViolationArb.map { it as Violation }
    }
