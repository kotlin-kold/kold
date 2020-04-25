package com.github.kold.validated

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ViolationTest : WordSpec() {
    init {
        "Violation.flatten" should {
            "preserve field name" {
                checkAll(Arb.string(), Arb.list(ValueViolationArb, 1..10)) { fieldName, violations ->
                    val violation: Violation = FieldViolation(fieldName, violations)
                    violation.flatten() shouldBe violations.map {
                        FlatViolation(fieldName, it.errorCode, it.description)
                    }.toSet()
                }
            }
            "use null as field name for ValueViolation" {
                checkAll(ValueViolationArb) { violation ->
                    violation.flatten() shouldBe setOf(
                        FlatViolation(null, violation.errorCode, violation.description)
                    )
                }
            }
            "use array suffix as field name for ElementsViolation" {
                checkAll(Arb.list(ValueViolationArb, 1..10), Arb.string()) { violations, arraySuffix ->
                    val violation = ElementsViolation(violations)
                    violation.flatten(arraySuffix = arraySuffix) shouldBe violations.map {
                        FlatViolation(arraySuffix, it.errorCode, it.description)
                    }.toSet()
                }
            }
            "use field name separator for sub-fields" {
                checkAll(
                    subFieldValueViolationsArb,
                    Arb.string(),
                    Arb.string()
                ) { subFieldViolations, fieldName, separator ->
                    val violation = FieldViolation(fieldName, subFieldViolations)
                    val expected = subFieldViolations.flatMap { subFieldViolation ->
                        subFieldViolation.violations.map { it as ValueViolation }.map {
                            FlatViolation(
                                "${fieldName}${separator}${subFieldViolation.fieldName}",
                                it.errorCode,
                                it.description
                            )

                        }
                    }.toSet()
                    violation.flatten(fieldNameSeparator = separator) shouldBe expected
                }
            }
            "use array suffix as suffix for array sub-fields violations" {
                checkAll(
                    subFieldElementsViolationsArb,
                    Arb.string(),
                    Arb.string(),
                    Arb.string()
                ) { subFieldViolations, fieldName, separator, arraySuffix ->
                    val violation = FieldViolation(fieldName, subFieldViolations)
                    val expected = subFieldViolations.flatMap { subFieldViolation ->
                        subFieldViolation.violations.map { it as ElementsViolation }.flatMap { elementsViolation ->
                            elementsViolation.violations.map { it as ValueViolation }.map {
                                FlatViolation(
                                    "${fieldName}${separator}${subFieldViolation.fieldName}${separator}${arraySuffix}",
                                    it.errorCode,
                                    it.description
                                )
                            }
                        }
                    }.toSet()
                    violation.flatten(fieldNameSeparator = separator, arraySuffix = arraySuffix) shouldBe expected
                }
            }
        }
    }

    private val subFieldValueViolationsArb: Arb<List<FieldViolation>> = Arb.list(
        Arb.bind(
            Arb.list(ValueViolationArb, 1..5),
            Arb.string()
        ) { violations, subFieldName ->
            FieldViolation(subFieldName, violations)
        },
        1..3
    )

    private val elementsValueViolationArb: Arb<List<ElementsViolation>> = Arb.list(
        Arb.list(ValueViolationArb, 1..3).map {
            ElementsViolation(it)
        }
    )

    private val subFieldElementsViolationsArb: Arb<List<FieldViolation>> = Arb.list(
        Arb.bind(
            elementsValueViolationArb,
            Arb.string()
        ) { violations, subFieldName ->
            FieldViolation(subFieldName, violations)
        },
        1..3
    )
}
