package com.paralainer.kold.examples

import com.paralainer.kold.context.combineValidations
import com.paralainer.kold.context.validationContext
import com.paralainer.kold.data.KoldData
import com.paralainer.kold.validated.Validated
import io.kotest.core.spec.style.StringSpec

class SimpleValidation : StringSpec() {

    private fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            combineValidations(
                require("name").validateValue { it.string() },
                opt("password").validateOption { it.string() },
                require("age").validateValue { it.long() },
                opt("score").validateOption { it.double() }
            ) { name, password, age, score ->
                User(name, password, age.toInt(), score)
            }
        }

    data class User(
        val name: String,
        val password: String?,
        val age: Int,
        val score: Double?
    )

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

    private val validInput = mapOf(
        "name" to "Jack",
        "password" to "secret",
        "age" to 42,
        "score" to 0.9
    ).let { KoldData.fromMap(it) }

    private val invalidInput = mapOf(
        "password" to "secret",
        "age" to 42,
        "score" to "0.9"
    ).let { KoldData.fromMap(it) }

    private val inputs = arrayOf(validInput, invalidInput)
}

