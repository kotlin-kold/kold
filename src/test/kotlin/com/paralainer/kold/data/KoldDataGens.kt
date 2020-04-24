package com.paralainer.kold.data

import com.paralainer.kold.utils.orNull
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string

val FlatKoldValueArb: Arb<KoldValue> =
    Arb.choice(
        Arb.string().map { KoldValue.fromString(it) },
        Arb.long().map { KoldValue.fromNumber(it) },
        Arb.double().map { KoldValue.fromNumber(it) },
        Arb.bool().map { KoldValue.fromBoolean(it) }
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

