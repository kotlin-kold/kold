package com.paralainer.kold.examples

import com.paralainer.kold.context.combineValidations
import com.paralainer.kold.context.validateOption
import com.paralainer.kold.context.validationContext
import com.paralainer.kold.data.KoldData
import com.paralainer.kold.validated.Validated
import io.kotest.core.spec.style.StringSpec

class CollectionValidation : StringSpec() {

    init {
        "print user" {
            for (input in inputs) {
                validateUser(input).orNull()?.let { println(it) }
                    ?: println("User is invalid")
            }
        }

        "print user or errors" {
            for (input in inputs) {
                validateUser(input).fold({
                    println(it.violations)
                }, {
                    println(it)
                })
            }
        }
    }

    private fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            combineValidations(
                require("name").validateValue { it.string() },
                opt("password").validateOption { it.string() },
                require("age").validateValue { it.long() },
                opt("score").validateOption { it.double() },
                require("phoneNumbers").validateValue {
                    it.list().validateElements { el -> el.notNull().string() }
                },
                opt("flags").validateOption {
                    it.list().validateElements { el -> el.validateOption { v -> v.long() } }
                }.default(emptyList()),
                opt("attributes").validateOption {
                    it.list().validateElements { el -> el.notNull().string() }
                }
            ) { name, password, age, score, phoneNumbers, flags, attributes ->
                User(name, password, age.toInt(), score, phoneNumbers, flags, attributes)
            }
        }

    data class User(
        val name: String,
        val password: String?,
        val age: Int,
        val score: Double?,
        val phoneNumbers: List<String>,
        val flags: List<Long?>,
        val attributes: List<String>?
    )


    private val validInput = mapOf(
        "name" to "Jack",
        "password" to "secret",
        "age" to 42,
        "score" to 0.9,
        "phoneNumbers" to listOf("+123", "+456"),
        "flags" to listOf(123, null, 456),
        "attributes" to null
    ).let { KoldData.fromMap(it) }

    private val invalidInput1 = mapOf(
        "password" to "secret",
        "age" to 42,
        "score" to "0.9"
    ).let { KoldData.fromMap(it) }

    private val invalidInput2 = mapOf(
        "password" to "secret",
        "age" to 42,
        "score" to "0.9",
        "phoneNumbers" to listOf("+123", 123, 456, null)
    ).let { KoldData.fromMap(it) }

    private val inputs = arrayOf(validInput, invalidInput1, invalidInput2)
}

