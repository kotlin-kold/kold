package com.paralainer.kold.utils

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


fun Arb.Companion.longRange(range: LongRange): Arb<Long> {
    val edgecases = mutableListOf(range.first, range.last)
    if (range.contains(0)) {
        edgecases.add(0)
    }
    return arb(LongShrinker, edgecases) { it.random.nextLong(range) }
}