package com.paralainer.kold.context

import com.paralainer.kold.data.BoolKoldValueArb
import com.paralainer.kold.data.DoubleKoldValueArb
import com.paralainer.kold.data.IntKoldValueArb
import com.paralainer.kold.data.KoldData
import com.paralainer.kold.data.KoldValue
import com.paralainer.kold.data.LongKoldValueArb
import com.paralainer.kold.data.StringKoldValueArb
import com.paralainer.kold.validated.OptionalField
import com.paralainer.kold.validated.Validated
import com.paralainer.kold.validated.ValueViolation
import com.paralainer.kold.validated.invalid
import com.paralainer.kold.validated.invalidArb
import com.paralainer.kold.validated.invalidField
import com.paralainer.kold.validated.valid
import com.paralainer.kold.validated.validArb
import com.paralainer.kold.validated.validField
import com.paralainer.kold.validated.validFieldArb
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ValidationContextTest : WordSpec() {
    init {
        "require" should {
            "return ValidatedField with Valid when exist" {
                checkAll(flatKoldDataGen()) { data ->
                    val testKey = data.data.keys.random()
                    testContext(data) {
                        val result = require(testKey)
                        result shouldBe data.data[testKey].validField(testKey)
                    }
                }
            }

            "return ValidatedField with nullValue violation when doesn't exist" {
                checkAll(flatKoldDataGen(), Arb.string(10..15)) { data, testKey ->
                    if (data.data.containsKey(testKey)) {
                        return@checkAll // skip this iteration
                    }

                    testContext(data) {
                        val result = require(testKey)
                        result shouldBe testConfig.nullValue.invalidField<KoldValue>(testKey)
                    }
                }
            }
        }

        "opt" should {
            "return OptionalField with KoldValue when exist" {
                checkAll(flatKoldDataGen()) { data ->
                    val testKey = data.data.keys.random()
                    testContext(data) {
                        val result = opt(testKey)
                        result shouldBe OptionalField(testKey, data[testKey])
                    }
                }
            }

            "return OptionalField with null when doesn't exist" {
                checkAll(flatKoldDataGen(), Arb.string(10..15)) { data, testKey ->
                    if (data.data.containsKey(testKey)) {
                        return@checkAll // skip this iteration
                    }

                    testContext(data) {
                        val result = opt(testKey)
                        result shouldBe OptionalField(testKey, null)
                    }
                }
            }
        }

        "default" should {
            "return original value when it's not null" {
                checkAll(validFieldArb(Arb.string() as Arb<String?>), Arb.string()) { validField, default ->
                    testContext {
                        validField.default(default) shouldBe validField
                    }
                }
            }
            "return default value when it's null" {
                checkAll(validFieldArb<String?>(Arb.constant(null)), Arb.string()) { validField, default ->
                    testContext {
                        validField.default(default) shouldBe default.validField(validField.fieldName)
                    }
                }
            }
        }

        "KoldValue.string" should {
            "return Validated.Valid<String> when it's string" {
                checkAll(StringKoldValueArb) { data ->
                    testContext {
                        data.string() shouldBe Validated.Valid(data.value as String)
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not string" {
                checkAll(LongKoldValueArb) { data ->
                    testContext {
                        data.string() shouldBe config.invalidValue.invalid()
                    }
                }
            }
        }

        "Validated<KoldValue>.string" should {
            "return Validated.Valid<String> when it's string" {
                checkAll(validArb(StringKoldValueArb)) { valid ->
                    testContext {
                        valid.string() shouldBe Validated.Valid(valid.value.value as String)
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not string" {
                checkAll(validArb(LongKoldValueArb)) { valid ->
                    testContext {
                        valid.string() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "pass Invalid when already Invalid" {
                checkAll(invalidArb<KoldValue>()) { invalid ->
                    testContext {
                        invalid.string() shouldBeSameInstanceAs invalid
                    }
                }
            }
        }

        "KoldValue.bool" should {
            "return Validated.Valid<Boolean> when it's Boolean" {
                checkAll(BoolKoldValueArb) { data ->
                    testContext {
                        data.bool() shouldBe Validated.Valid(data.value as Boolean)
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Boolean" {
                checkAll(LongKoldValueArb) { data ->
                    testContext {
                        data.bool() shouldBe config.invalidValue.invalid()
                    }
                }
            }
        }

        "Validated<KoldValue>.bool" should {
            "return Validated.Valid<Boolean> when it's Boolean" {
                checkAll(validArb(BoolKoldValueArb)) { valid ->
                    testContext {
                        valid.bool() shouldBe Validated.Valid(valid.value.value as Boolean)
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not boolean" {
                checkAll(validArb(LongKoldValueArb)) { valid ->
                    testContext {
                        valid.bool() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "pass Invalid when already Invalid" {
                checkAll(invalidArb<KoldValue>()) { invalid ->
                    testContext {
                        invalid.bool() shouldBeSameInstanceAs invalid
                    }
                }
            }
        }

        "KoldValue.int" should {
            "return Validated.Valid<Int> when it's Int" {
                checkAll(IntKoldValueArb) { data ->
                    testContext {
                        data.int() shouldBe Validated.Valid(data.value as Int)
                    }
                }
            }

            "return Validated.Valid<Int> when it's Long that fits in Int" {
                checkAll(validIntLongGen) { value ->
                    testContext {
                        KoldValue.fromNumber(value).int() shouldBe Validated.Valid(value.toInt())
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Int" {
                checkAll(DoubleKoldValueArb) { data ->
                    testContext {
                        data.int() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Int" {
                checkAll(DoubleKoldValueArb) { data ->
                    testContext {
                        data.int() shouldBe config.invalidValue.invalid()
                    }
                }
            }
        }

        "Validated<KoldValue>.int" should {
            "return Validated.Valid<Int> when it's Int" {
                checkAll(validArb(IntKoldValueArb)) { valid ->
                    testContext {
                        valid.int() shouldBe Validated.Valid(valid.value.value as Int)
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Int" {
                checkAll(validArb(DoubleKoldValueArb)) { valid ->
                    testContext {
                        valid.int() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "pass Invalid when already Invalid" {
                checkAll(invalidArb<KoldValue>()) { invalid ->
                    testContext {
                        (1..10).min()
                        invalid.int() shouldBeSameInstanceAs invalid
                    }
                }
            }
        }
    }

    private val testConfig = ValidationContextConfig(
        nullValue = ValueViolation("null.error", "test description"),
        invalidValue = ValueViolation("invalid.error", "test description"),
        intOverflow = ValueViolation("int.overflow.error", "test description")
    )

    private val emptyData: KoldData = KoldData(emptyMap())

    private val validIntLongGen: Arb<Long> = Arb.long(Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong())



    private val overflowIntLongGen: Arb<Long> = Arb.choice(
        Arb.long(Long.MIN_VALUE until Int.MIN_VALUE.toLong()),
        Arb.long((Int.MAX_VALUE.toLong() + 1L)..Long.MAX_VALUE)
    )

    private fun testContext(data: KoldData = emptyData, validate: ValidationContext.() -> Unit): Validated<Unit> =
        data.validationContext(testConfig) {
            validate()
            Unit.valid()
        }

    private fun flatKoldDataGen(
        valueGen: Arb<KoldValue> = Arb.string().map { KoldValue.fromString(it) }
    ): Arb<KoldData> =
        Arb.map(
            Arb.string(1..10),
            valueGen,
            1, 10
        ).map(::KoldData)
}

