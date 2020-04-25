package com.github.kold.examples

import com.github.kold.context.combineFields
import com.github.kold.context.validationContext
import com.github.kold.data.KoldData
import com.github.kold.validated.Validated

object SimpleValidation {

    fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            combineFields(
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
}

