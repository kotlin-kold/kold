package com.github.kold.validated

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
            "convert value when it's ValidField" {
                checkAll(validFieldArb(Arb.string())) { validField ->
                    val result = validField.convertValue { it.toUpperCase() }

                    result shouldBe ValidField(
                        validField.fieldName,
                        validField.value.toUpperCase()
                    )
                }
            }

            "doesn't modify value when it's InvalidField" {
                checkAll(invalidFieldArb<String>()) { invalidField ->
                    val result = invalidField.convertValue { it.toUpperCase() }

                    result shouldBeSameInstanceAs invalidField
                }
            }
        }

        "ValidatedField.validateValue" should {
            "convert value when it's ValidField" {
                checkAll(validFieldArb(Arb.string())) { validField ->
                    val result = validField.validateValue { Validated.Valid(it.toUpperCase()) }

                    result shouldBe ValidField(
                        validField.fieldName,
                        validField.value.toUpperCase()
                    )
                }
            }

            "convert value when it's ValidField and validation result is Invalid " {
                checkAll(validFieldArb(Arb.string()), invalidArb<Int>()) { validField, invalid ->
                    val result = validField.validateValue { invalid }

                    result shouldBe InvalidField(
                        FieldViolation(
                            validField.fieldName,
                            invalid.violations
                        )
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
            "wrap T into ValidField" {
                checkAll(Arb.string(), Arb.string()) { value, fieldName ->
                    value.validField(fieldName) shouldBe ValidField(fieldName, value)
                }
            }
        }

        "Violation.invalidField" should {
            "wrap Violation into InvalidField" {
                checkAll(ValueViolationArb, Arb.string()) { violation, fieldName ->
                    violation.invalidField<Any>(fieldName) shouldBe InvalidField(
                        FieldViolation(
                            fieldName,
                            listOf(violation)
                        )
                    )
                }
            }
        }
    }
}
