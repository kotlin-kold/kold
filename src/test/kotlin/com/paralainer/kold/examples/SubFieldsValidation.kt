/*
package com.paralainer.kold.examples

import com.paralainer.kold.context.validateFields
import com.paralainer.kold.context.validationContext
import com.paralainer.kold.data.KoldData
import com.paralainer.kold.validated.Validated
import io.kotest.core.spec.style.StringSpec

class SubFieldsValidation : StringSpec() {

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
                    println(it.flatten())
                }, {
                    println(it)
                })
            }
        }
    }

    private fun validatePhoneNumber(input: KoldData): Validated<PhoneNumber> =
        input.validationContext {
            validateFields(
                opt("type").validateOption { type -> type.string() },
                require("value").validateValue { value -> value.string() },
                require("index").validateValue { value -> value.int() }
            ) { type, value, index ->
                PhoneNumber(type, value, index)
            }
        }

    private fun validateSettings(input: KoldData): Validated<Settings> =
        input.validationContext {
            validateFields(
                require("verified").validateValue { it.bool() },
                opt("tags").validateOption {
                    it.list().validateElements { el -> el.notNull().string() }
                }.default(emptyList()),
                opt("language").validateOption { it.string() }
            ) { verified, tags, language ->
                Settings(verified, tags, language)
            }
        }

    private fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            validateFields(
                require("name").validateValue { it.string() },
                opt("password").validateOption { it.string() },
                require("age").validateValue { it.long() },
                opt("score").validateOption { it.double() },
                require("phoneNumbers").validateValue {
                    it.list().validateElements { el ->
                        el.notNull().obj().flatMap { data -> validatePhoneNumber(data) }
                    }
                },
                require("settings").validateValue {
                    it.obj().flatMap { data -> validateSettings(data) }
                }
            ) { name, password, age, score, phoneNumbers, settings ->
                User(name, password, age.toInt(), score, phoneNumbers, settings)
            }
        }


    data class User(
        val name: String,
        val password: String?,
        val age: Int,
        val score: Double?,
        val phoneNumbers: List<PhoneNumber>,
        val settings: Settings
    )

    data class Settings(
        val verified: Boolean,
        val tags: List<String>,
        val language: String?
    )

    data class PhoneNumber(
        val type: String?,
        val value: String,
        val index: Int
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
        "phoneNumbers" to listOf(
            mapOf("type" to "some", "value" to "123"), 123, 456, null)
    ).let { KoldData.fromMap(it) }

    private val inputs = arrayOf(validInput, invalidInput1, invalidInput2)
}


*/
