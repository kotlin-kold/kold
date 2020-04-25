package com.github.kold.data

import com.github.kold.utils.number
import com.github.kold.utils.orNull
import com.github.kold.validated.Validated
import com.github.kold.validated.ValueViolationArb
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class KoldValueTest : WordSpec() {
    init {
        "KoldValue.string()" should {
            "return Valid for string values" {
                checkAll(Arb.string()) { value ->
                    KoldValue.fromString(value)
                        .string(onInvalid = { throw Exception() }) shouldBe Validated.Valid(value)
                }
            }
            "return Invalid for non-string values" {
                checkAll(Arb.int(), ValueViolationArb) { value, violation ->
                    KoldValue.fromNumber(value).string(onInvalid = { violation }) shouldBe Validated.Invalid(
                        listOf(
                            violation
                        )
                    )
                }
            }
        }

        "KoldValue.number()" should {
            "return Valid for number values" {
                checkAll(Arb.number()) { value ->
                    KoldValue.fromNumber(value)
                        .number(onInvalid = { throw Exception() }) shouldBe Validated.Valid(value)
                }
            }
            "return Invalid for non-number values" {
                checkAll(Arb.string(), ValueViolationArb) { value, violation ->
                    KoldValue.fromString(value).number(onInvalid = { violation }) shouldBe Validated.Invalid(
                        listOf(
                            violation
                        )
                    )
                }
            }
        }

        "KoldValue.boolean()" should {
            "return Valid for boolean values" {
                checkAll(Arb.bool()) { value ->
                    KoldValue.fromBoolean(value)
                        .bool(onInvalid = { throw Exception() }) shouldBe Validated.Valid(value)
                }
            }
            "return Invalid for non-boolean values" {
                checkAll(Arb.string(), ValueViolationArb) { value, violation ->
                    KoldValue.fromString(value).bool(onInvalid = { violation }) shouldBe Validated.Invalid(
                        listOf(
                            violation
                        )
                    )
                }
            }
        }

        "KoldValue.list()" should {
            "return Valid for collection values" {
                checkAll(Arb.set(koldValueArb().orNull(), 1..10)) { value ->
                    KoldValue.fromCollection(value)
                        .list(onInvalid = { throw Exception() }) shouldBe Validated.Valid(value.toList())
                }
            }
            "return Invalid for non-collection values" {
                checkAll(Arb.string(), ValueViolationArb) { value, violation ->
                    KoldValue.fromString(value).list(onInvalid = { violation }) shouldBe Validated.Invalid(
                        listOf(
                            violation
                        )
                    )
                }
            }
        }

        "KoldValue.obj()" should {
            "return Valid for KoldData values" {
                checkAll(koldDataArb()) { value ->
                    KoldValue.fromObject(value)
                        .obj(onInvalid = { throw Exception() }) shouldBe Validated.Valid(value)
                }
            }
            "return Invalid for non-KoldData values" {
                checkAll(Arb.string(), ValueViolationArb) { value, violation ->
                    KoldValue.fromString(value).obj(onInvalid = { violation }) shouldBe Validated.Invalid(
                        listOf(
                            violation
                        )
                    )
                }
            }
        }
    }
}
