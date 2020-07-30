package com.github.kold.context

import com.github.kold.utils.number
import com.github.kold.validated.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class CombineFieldsTest : WordSpec() {
    init {
        "combineFields with 1 arg" should {
            "call combine function with correct args when Valid" {
                checkAll(Arb.string(), Arb.string()) { fieldName, value ->
                    val result = combineFields(value.validField(fieldName)) { it }
                    result shouldBe value.valid()
                }
            }

            "return FieldViolation when field is Invalid" {
                checkAll(Arb.string(), ValueViolationArb) { fieldName, violation ->
                    val result = combineFields(violation.invalidField<Any>(fieldName)) { it }
                    result shouldBe FieldViolation(fieldName, listOf(violation)).invalid()
                }
            }
        }

        "combineFields with 2 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(2)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1])
                    ) { arg1, arg2 -> listOf(arg1, arg2) }
                    result shouldBe listOf(args[0].second, args[1].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(2)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1]
                    ) { _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }

        "combineFields with 3 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(3)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2])
                    ) { arg1, arg2, arg3 -> listOf(arg1, arg2, arg3) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(3)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2]
                    ) { _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }

        "combineFields with 4 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(4)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3])
                    ) { arg1, arg2, arg3, arg4 -> listOf(arg1, arg2, arg3, arg4) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(4)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3]
                    ) { _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }

        "combineFields with 5 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(5)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4])
                    ) { arg1, arg2, arg3, arg4, arg5 -> listOf(arg1, arg2, arg3, arg4, arg5) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(5)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4]
                    ) { _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }

        "combineFields with 6 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(6)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4]),
                        toValidField(args[5])
                    ) { arg1, arg2, arg3, arg4, arg5, arg6 -> listOf(arg1, arg2, arg3, arg4, arg5, arg6) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second, args[5].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(6)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4],
                        someInvalidFields.fields[5]
                    ) { _, _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }
        "combineFields with 7 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(7)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4]),
                        toValidField(args[5]),
                        toValidField(args[6])
                    ) { arg1, arg2, arg3, arg4, arg5, arg6, arg7 -> listOf(arg1, arg2, arg3, arg4, arg5, arg6, arg7) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second, args[5].second, args[6].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(7)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4],
                        someInvalidFields.fields[5],
                        someInvalidFields.fields[6]
                    ) { _, _, _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }
        "combineFields with 8 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(8)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4]),
                        toValidField(args[5]),
                        toValidField(args[6]),
                        toValidField(args[7])
                    ) { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 -> listOf(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second, args[5].second, args[6].second, args[7].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(8)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4],
                        someInvalidFields.fields[5],
                        someInvalidFields.fields[6],
                        someInvalidFields.fields[7]
                    ) { _, _, _, _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }
        "combineFields with 9 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(9)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4]),
                        toValidField(args[5]),
                        toValidField(args[6]),
                        toValidField(args[7]),
                        toValidField(args[8])
                    ) { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9 -> listOf(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second, args[5].second, args[6].second, args[7].second, args[8].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(9)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4],
                        someInvalidFields.fields[5],
                        someInvalidFields.fields[6],
                        someInvalidFields.fields[7],
                        someInvalidFields.fields[8]
                    ) { _, _, _, _, _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }
        "combineFields with 10 args" should {
            "call combine function with correct args when Valid" {
                checkAll(validArgumentsArb(10)) { args ->
                    val result = combineFields(
                        toValidField(args[0]),
                        toValidField(args[1]),
                        toValidField(args[2]),
                        toValidField(args[3]),
                        toValidField(args[4]),
                        toValidField(args[5]),
                        toValidField(args[6]),
                        toValidField(args[7]),
                        toValidField(args[8]),
                        toValidField(args[9])
                    ) { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10 -> listOf(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) }
                    result shouldBe listOf(args[0].second, args[1].second, args[2].second, args[3].second, args[4].second, args[5].second, args[6].second, args[7].second, args[8].second, args[9].second).valid()
                }
            }

            "return Violations when some fields are Invalid" {
                checkAll(someInvalidArb(10)) { someInvalidFields ->
                    val result = combineFields(
                        someInvalidFields.fields[0],
                        someInvalidFields.fields[1],
                        someInvalidFields.fields[2],
                        someInvalidFields.fields[3],
                        someInvalidFields.fields[4],
                        someInvalidFields.fields[5],
                        someInvalidFields.fields[6],
                        someInvalidFields.fields[7],
                        someInvalidFields.fields[8],
                        someInvalidFields.fields[9]
                    ) { _, _, _, _, _, _, _, _, _, _ -> Unit }
                    val invalid = result as Validated.Invalid
                    invalid.violations.toSet() shouldBe someInvalidFields.violations
                }
            }
        }

    }

    private fun toValidField(pair: Pair<String, Any>): ValidatedField<Any> =
        pair.second.validField(pair.first)

    private fun validArgumentsArb(size: Int): Arb<List<Pair<String, Any>>> =
        Arb.list(
            Arb.bind(
                Arb.string(),
                argumentArb
            ) { fieldName, value -> fieldName to value },
            size..size
        )

    private val argumentArb: Arb<Any> =
        Arb.choice(
            Arb.string(),
            Arb.number()
        )

    private fun someInvalidArb(size: Int): Arb<SomeInvalidFields> =
        Arb.list(invalidFieldArb<Any>(), 1..size).flatMap { invalidFields ->
            val violations = invalidFields.map { it.violation }.toSet()

            if (invalidFields.size == size) {
                Arb.constant(SomeInvalidFields(invalidFields, violations))
            } else {
                validArgumentsArb(size - invalidFields.size).map { validFields ->
                    val allFields = invalidFields.plus(validFields.map { toValidField(it) })
                    SomeInvalidFields(allFields.shuffled(), violations)
                }
            }
        }

    private class SomeInvalidFields(val fields: List<ValidatedField<Any>>, val violations: Set<Violation>)
}
