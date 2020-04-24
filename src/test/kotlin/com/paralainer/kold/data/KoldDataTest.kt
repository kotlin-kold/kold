package com.paralainer.kold.data

import com.paralainer.kold.utils.number
import com.paralainer.kold.utils.orNull
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class KoldDataTest : WordSpec() {
    init {
        "KoldData.fromMap" should {
            "parse into KoldData when all the values are correct" {
                checkAll(koldDataArb()) { data ->
                    val map = toMap(data.data)
                    val result = KoldData.fromMap(map)
                    result.data shouldBe data.data
                }
            }
            "throw KoldDataParsingException when keys are not of type string" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        Arb.map(
                            Arb.number(), Arb.string(), 1, 10
                        ),
                        1, 10
                    )
                ) { map ->
                    shouldThrow<KoldDataParsingException> {
                        KoldData.fromMap(map)
                    }.message shouldBe "Only keys of type string are supported"
                }
            }

            "throw KoldDataParsingException when values are not of supported type" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        CustomClassGen,
                        1, 10
                    )
                ) { map ->
                    shouldThrow<KoldDataParsingException> {
                        KoldData.fromMap(map)
                    }.message shouldBe "Value of unexpected type ${CustomClass::class} found"
                }
            }
        }

        "KoldData.get" should {
            "return value when exist" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        Arb.string(),
                        1, 10
                    )
                ) { map ->
                    val result = KoldData.fromMap(map)
                    map.forEach {
                        result[it.key] shouldBe KoldValue.fromString(it.value)
                    }
                }
            }

            "return null when doesn't exist" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        Arb.string(),
                        1, 10
                    ),
                    Arb.string(10..15)
                ) { map, key ->

                    val result = KoldData.fromMap(map)
                    if (!map.containsKey(key)) {
                        result[key] shouldBe null
                    }
                }
            }
        }

        "KoldData.contains" should {
            "return true when exist" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        Arb.string(),
                        1, 10
                    )
                ) { map ->
                    val result = KoldData.fromMap(map)
                    map.forEach {
                        result.contains(it.key) shouldBe true
                    }
                }
            }

            "return false when doesn't exist" {
                checkAll(
                    Arb.map(
                        Arb.string(),
                        Arb.string(),
                        1, 10
                    ),
                    Arb.string(10..15)
                ) { map, key ->

                    val result = KoldData.fromMap(map)
                    if (!map.containsKey(key)) {
                        result.contains(key) shouldBe false
                    }
                }
            }
        }
    }


    private fun toMap(data: Map<String, KoldValue?>): Map<String, Any?> =
        data.mapValues {
            val value = it.value?.value
            if (value is KoldData) {
                toMap(value.data)
            } else if (value is List<*>) {
                toCollection(value as List<KoldValue?>)
            } else {
                value
            }
        }

    private fun toCollection(list: List<KoldValue?>): List<Any?> =
        list.map {
            val value = it?.value
            if (value is KoldData) {
                toMap(value.data)
            } else if (value is List<*>) {
                toCollection(value as List<KoldValue?>)
            } else {
                value
            }
        }

    private val flatCorrectValue: Arb<Any> = Arb.choice(
        Arb.number(),
        Arb.string(),
        Arb.bool()
    )

    private fun correctValue(maxDepth: Int = 3): Arb<Any> =
        if (maxDepth > 1)
            Arb.choice(
                flatCorrectValue,
                Arb.list(correctValue(maxDepth - 1).orNull(), 1..10),
                Arb.map(Arb.string(), correctValue(maxDepth - 1))
            )
        else
            flatCorrectValue

    private val CustomClassGen = Arb.string().map { CustomClass(it) }

    private data class CustomClass(val value: String)

}
