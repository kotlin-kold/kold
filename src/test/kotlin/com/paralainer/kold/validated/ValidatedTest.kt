package com.paralainer.kold.validated

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ValidatedTest : WordSpec() {
    init {
        "Validated.flatMap" should {
            "transform value when Valid" {
                checkAll(validArb(Arb.string())) { valid ->
                    valid.flatMap {
                        Validated.Valid(it.toUpperCase())
                    } shouldBe Validated.Valid(valid.value.toUpperCase())
                }
            }
            "transform value when Valid but result is invalid" {
                checkAll(validArb(Arb.string()), invalidArb<Int>()) { valid, invalid ->
                    valid.flatMap {
                        invalid
                    } shouldBe invalid
                }
            }

            "doesn't modify Invalid" {
                checkAll(
                    invalidArb<Int>(),
                    Arb.choice<Validated<Int>>(validArb(Arb.int()), invalidArb<Int>())
                ) { invalid, transformation ->
                    invalid.flatMap {
                        transformation
                    } shouldBeSameInstanceAs (invalid)
                }
            }
        }

        "Validated.fold" should {
            "call onValid for Valid" {
                checkAll(validArb(Arb.string())) { valid ->
                    val value = valid.fold(
                        onValid = {
                            valid.value.toUpperCase()

                        },
                        onInvalid = {
                            throw Exception()
                        }
                    )
                    value shouldBe valid.value.toUpperCase()
                }
            }
            "call onInvalid for Invalid" {
                checkAll(Arb.string(), invalidArb<Int>()) { value, invalid ->
                    val expected = invalid.fold(
                        onValid = {
                            throw Exception()
                        },
                        onInvalid = {
                            value
                        }
                    )
                    expected shouldBe expected
                }
            }
        }

        "Validated.orNull" should {
            "return value for Valid" {
                checkAll(validArb(Arb.string())) { valid ->
                    val value = valid.orNull()
                    value shouldBe valid.value
                }
            }
            "return null for Invalid" {
                checkAll(invalidArb<Int>()) { invalid ->
                    val value = invalid.orNull()
                    value shouldBe null
                }
            }
        }

        "Validated.Invalid.flatten" should {
            "combine flatten results from each violation" {
                checkAll(Arb.list(violationArb(), 1..10), Arb.string(), Arb.string()) { violations, separator, suffix ->
                    val expected = violations.flatMap {
                        it.flatten(fieldNameSeparator = separator, arraySuffix = suffix)
                    }.toSet()

                    Validated.Invalid<Any>(violations)
                        .flatten(fieldNameSeparator = separator, arraySuffix = suffix) shouldBe expected
                }
            }

            "combine flatten results from each violation with default args" {
                checkAll(Arb.list(violationArb(), 1..10)) { violations ->
                    val expected = violations.flatMap {
                        it.flatten()
                    }.toSet()

                    Validated.Invalid<Any>(violations)
                        .flatten() shouldBe expected
                }
            }
        }

        "T.valid" should {
            "wrap T into Valid" {
                checkAll(Arb.string()) { value ->
                    value.valid() shouldBe Validated.Valid(value)
                }
            }
        }

        "Violation.invalid" should {
            "wrap Violation into Invalid" {
                checkAll(violationArb()) { violation ->
                    violation.invalid<Any>() shouldBe Validated.Invalid(listOf(violation))
                }
            }
        }
    }
}
