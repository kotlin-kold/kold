package com.paralainer.kold.validated

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ValidatedFieldTest : WordSpec() {
    init {
        "ValidatedField.convertValue" should {
            "convert value when it's Valid" {
                checkAll(validFieldArb(Arb.string())) { validField ->
                    val result = validField.convertValue { it.toUpperCase() }

                    result shouldBe ValidatedField(
                        validField.fieldName,
                        Validated.Valid((validField.value as Validated.Valid<String>).value.toUpperCase())
                    )
                }
            }

            "doesn't modify value when it's Invalid" {
                checkAll(invalidFieldArb<String>()) { invalidField ->
                    val result = invalidField.convertValue { it.toUpperCase() }

                    result shouldBe ValidatedField(
                        invalidField.fieldName,
                        invalidField.value
                    )

                    invalidField.value shouldBeSameInstanceAs result.value
                }
            }
        }

        "ValidatedField.validateValue" should {
            "convert value when it's Valid" {
                checkAll(validFieldArb(Arb.string())) { validField ->
                    val result = validField.validateValue { Validated.Valid(it.toUpperCase()) }

                    result shouldBe ValidatedField(
                        validField.fieldName,
                        Validated.Valid((validField.value as Validated.Valid<String>).value.toUpperCase())
                    )
                }
            }

            "convert value when it's Valid and validation result is Invalid " {
                checkAll(validFieldArb(Arb.string()), invalidArb<Int>()) { validField, invalid ->
                    val result = validField.validateValue { invalid }

                    result shouldBe ValidatedField(
                        validField.fieldName,
                        Validated.Invalid<Int>(listOf(FieldViolation(validField.fieldName, invalid.violations)))
                    )
                }
            }

            "doesn't modify value when it's Invalid" {
                checkAll(invalidFieldArb<String>(), Arb.int()) { invalidField, transformationResult ->
                    val result = invalidField.validateValue<Int> { Validated.Valid(transformationResult) }

                    result shouldBeSameInstanceAs invalidField
                }
            }
        }

        "T.validField" should {
            "wrap T into ValidatedField with Valid value" {
                checkAll(Arb.string(), Arb.string()) { value, fieldName ->
                    value.validField(fieldName) shouldBe ValidatedField(fieldName, Validated.Valid(value))
                }
            }
        }

        "Violation.invalidField" should {
            "wrap ValueViolation into ValidatedField with Invalid value" {
                checkAll(ValueViolationArb, Arb.string()) { violation, fieldName ->
                    violation.invalidField<Any>(fieldName) shouldBe ValidatedField<Any>(
                        fieldName, Validated.Invalid(
                            listOf(FieldViolation(fieldName, listOf(violation)))
                        )
                    )
                }
            }
        }
    }
}
