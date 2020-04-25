package com.paralainer.kold.context

import com.paralainer.kold.data.*
import com.paralainer.kold.utils.longRange
import com.paralainer.kold.utils.number
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

            "return Validated.Valid<Int> when it's Long that fits in Int" {
                checkAll(validArb(validIntLongGen)) { valid ->
                    testContext {
                        valid.int() shouldBe Validated.Valid((valid.value.value as Long).toInt())
                    }
                }
            }

            "return Validated.Invalid with intOverflow when it's Long that doesn't fit in Int" {
                checkAll(validArb(overflowIntLongGen)) { valid ->
                    testContext {
                        valid.int() shouldBe config.intOverflow.invalid()
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

        "Validated<KoldValue>.long" should {
            "return Validated.Valid<Long> when it's Whole Number" {
                checkAll(validArb(WholeNumberKoldValueArb)) { valid ->
                    testContext {
                        valid.long() shouldBe Validated.Valid((valid.value.value as Number).toLong())
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Long" {
                checkAll(validArb(DoubleKoldValueArb)) { valid ->
                    testContext {
                        valid.long() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "pass Invalid when already Invalid" {
                checkAll(invalidArb<KoldValue>()) { invalid ->
                    testContext {
                        invalid.long() shouldBeSameInstanceAs invalid
                    }
                }
            }
        }

        "Validated<KoldValue>.double" should {
            "return Validated.Valid<Double> when it's Number" {
                checkAll(validArb(NumberKoldValueArb)) { valid ->
                    testContext {
                        valid.double() shouldBe Validated.Valid((valid.value.value as Number).toDouble())
                    }
                }
            }

            "return Validated.Invalid with invalidValue when it's not Number" {
                checkAll(validArb(StringKoldValueArb)) { valid ->
                    testContext {
                        valid.long() shouldBe config.invalidValue.invalid()
                    }
                }
            }

            "pass Invalid when already Invalid" {
                checkAll(invalidArb<KoldValue>()) { invalid ->
                    testContext {
                        invalid.long() shouldBeSameInstanceAs invalid
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

    private val validIntLongGen: Arb<KoldValue> = Arb.longRange(Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()).map { KoldValue.fromNumber(it) }

    private val overflowIntLongGen: Arb<KoldValue> = Arb.choice(
        Arb.longRange(Long.MIN_VALUE until Int.MIN_VALUE.toLong()),
        Arb.longRange((Int.MAX_VALUE.toLong() + 1L)..Long.MAX_VALUE)
    ).map { KoldValue.fromNumber(it) }

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

