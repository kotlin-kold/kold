package com.github.kold.examples

import com.github.kold.context.combineFields
import com.github.kold.context.validateElements
import com.github.kold.context.default
import com.github.kold.context.validateOption
import com.github.kold.context.validationContext
import com.github.kold.data.KoldData
import com.github.kold.validated.Validated

object CollectionValidation {

    fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            combineFields(
                require("name").validateValue { it.string() },
                opt("password").validateOption { it.string() },
                require("age").validateValue { it.int() },
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
                User(name, password, age, score, phoneNumbers, flags, attributes)
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
}

