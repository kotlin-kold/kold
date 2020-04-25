package com.paralainer.kold.data

import com.paralainer.kold.utils.number
import com.paralainer.kold.utils.orNull
import com.paralainer.kold.utils.wholeNumber
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string

val StringKoldValueArb = Arb.string().map { KoldValue.fromString(it) }
val LongKoldValueArb = Arb.long().map { KoldValue.fromNumber(it) }
val IntKoldValueArb = Arb.int().map { KoldValue.fromNumber(it) }
val DoubleKoldValueArb = Arb.double().map { KoldValue.fromNumber(it) }
val BoolKoldValueArb = Arb.bool().map { KoldValue.fromBoolean(it) }
val NumberKoldValueArb = Arb.number().map { KoldValue.fromNumber(it) }
val WholeNumberKoldValueArb = Arb.wholeNumber().map { KoldValue.fromNumber(it) }

val FlatKoldValueArb: Arb<KoldValue> =
    Arb.choice(
        StringKoldValueArb,
        BoolKoldValueArb,
        NumberKoldValueArb
    )

fun koldDataArb(maxDepth: Int = 2): Arb<KoldData> =
    Arb.map(Arb.string(), koldValueArb(maxDepth).orNull(), minSize = 1, maxSize = 10).map {
        KoldData(it)
    }

fun koldValueArb(maxDepth: Int = 2): Arb<KoldValue> =
    if (maxDepth > 1) {
        Arb.choice(
            FlatKoldValueArb,
            Arb.list(koldValueArb(maxDepth - 1).orNull(), 1..10).map { KoldValue.fromCollection(it) },
            koldDataArb(maxDepth - 1).map { KoldValue.fromObject(it) }
        )
    } else {
        FlatKoldValueArb
    }

