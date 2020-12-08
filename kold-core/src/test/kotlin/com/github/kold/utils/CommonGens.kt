package com.github.kold.utils

import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import kotlin.random.nextLong

fun <T> Arb<T>.orNull(): Arb<T?> = Arb.choice(this, Arb.constant(null))

fun Arb.Companion.wholeNumber(): Arb<Number> = Arb.choice(
    Arb.int(),
    Arb.short(),
    Arb.long()
)

fun Arb.Companion.number(): Arb<Number> = Arb.choice(
    Arb.double(),
    Arb.float(),
    Arb.wholeNumber()
)
