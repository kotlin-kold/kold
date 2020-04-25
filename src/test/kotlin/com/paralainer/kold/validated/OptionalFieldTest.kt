package com.paralainer.kold.validated

import com.paralainer.kold.data.koldValueArb
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class OptionalFieldTest : WordSpec() {
    init {
        "OptionalField.validateOption" should {
            "convert value when it's not null" {
                checkAll(
                    koldValueArb(),
                    Arb.string(),
                    Arb.choice<Validated<Int>>(validArb(Arb.int()), invalidArb())
                ) { koldValue, fieldName, validationResult ->
                    val result = OptionalField(fieldName, koldValue).validateOption {
                        it shouldBe koldValue
                        validationResult
                    }

                    result shouldBe validationResult.validatedField(fieldName)
                }
            }

            "use null as valid value when it' koldValue = null" {
                checkAll(
                    Arb.string(),
                    Arb.choice<Validated<Int>>(validArb(Arb.int()), invalidArb())
                ) { fieldName, validationResult ->
                    val result = OptionalField(fieldName, null).validateOption {
                        validationResult
                    }

                    result shouldBe ValidField(fieldName, null)
                }
            }
        }
    }
}
