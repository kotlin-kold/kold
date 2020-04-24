package com.paralainer.kold.utils

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.short

fun <T> Arb<T>.orNull(): Arb<T?> = Arb.choice(this, Arb.constant(null))

fun Arb.Companion.number(): Arb<Number> = Arb.choice(
    Arb.double(),
    Arb.float(),
    Arb.int(),
    Arb.short(),
    Arb.long()
)
