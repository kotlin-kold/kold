package com.paralainer.kold.context

import com.paralainer.kold.data.KoldData
import com.paralainer.kold.data.KoldValue
import com.paralainer.kold.validated.ElementsViolation
import com.paralainer.kold.validated.OptionalField
import com.paralainer.kold.validated.Validated
import com.paralainer.kold.validated.ValidatedField
import com.paralainer.kold.validated.invalid
import com.paralainer.kold.validated.invalidField
import com.paralainer.kold.validated.valid
import com.paralainer.kold.validated.validField

class ValidationContext(
    val data: KoldData,
    val config: ValidationContextConfig = ValidationContextConfig()
) {

    fun require(fieldName: String): ValidatedField<KoldValue> =
        data[fieldName]?.validField(fieldName) ?: config.nullValue.invalidField(fieldName)

    fun opt(fieldName: String): OptionalField =
        OptionalField(fieldName, data[fieldName])

    fun <T> ValidatedField<T?>.default(default: T): ValidatedField<T> =
        this.convertValue { it ?: default }

    fun KoldValue.string(): Validated<String> = this.string { config.invalidValue }
    fun Validated<KoldValue>.string(): Validated<String> = this.flatMap { it.string() }

    fun KoldValue.bool(): Validated<Boolean> = this.bool { config.invalidValue }
    fun Validated<KoldValue>.bool(): Validated<Boolean> = this.flatMap { it.bool() }

    fun KoldValue.int(): Validated<Int> =
        this.number { config.invalidValue }.flatMap { it.safeToInt() }

    fun Validated<KoldValue>.int(): Validated<Int> = this.flatMap { it.int() }

    fun KoldValue.long(): Validated<Long> =
        this.number { config.invalidValue }.flatMap { it.safeToLong() }

    fun Validated<KoldValue>.long(): Validated<Long> = this.flatMap { it.long() }

    fun KoldValue.double(): Validated<Double> =
        this.number { config.invalidValue }.flatMap { it.toDouble().valid() }

    fun Validated<KoldValue>.double(): Validated<Double> = this.flatMap { it.double() }

    fun KoldValue.list(): Validated<List<KoldValue?>> =
        this.list { config.invalidValue }

    fun KoldValue.obj(): Validated<KoldData> =
        this.obj { config.invalidValue }

    fun Validated<KoldValue>.obj(): Validated<KoldData> = flatMap { it.obj() }

    fun <R> R?.notNull(): Validated<R> =
        this?.valid() ?: config.nullValue.invalid()

    fun <T, R> Validated<List<T>>.validateElements(block: (T) -> Validated<R>): Validated<List<R>> =
        this.flatMap { list ->
            val validatedElements = list.map { block(it) }
            val violations = validatedElements.mapNotNull {
                when (it) {
                    is Validated.Valid -> null
                    is Validated.Invalid -> it.violations
                }
            }.flatten().toSet()

            if (violations.isEmpty()) {
                validatedElements.map { (it as Validated.Valid<R>).value }.valid()
            } else {
                ElementsViolation(violations.toList()).invalid()
            }
        }


    private fun Number.safeToInt(): Validated<Int> =
        this.safeToLong().flatMap { value ->
            if (value > Int.MAX_VALUE || value < Int.MIN_VALUE)
                config.intOverflow.invalid()
            else
                value.toInt().valid()
        }

    private fun Number.safeToLong(): Validated<Long> =
        when (this) {
            is Long -> this.valid()
            is Int -> this.toLong().valid()
            is Short -> this.toLong().valid()
            is Byte -> this.toLong().valid()
            else -> config.invalidValue.invalid()
        }
}

fun <T, R> T?.validateOption(block: (T) -> Validated<R>): Validated<R?> =
    if (this == null)
        Validated.Valid(null)
    else
        block(this).flatMap { Validated.Valid(it as R?) }


fun <R> KoldData.validationContext(
    config: ValidationContextConfig = ValidationContextConfig(),
    validation: ValidationContext.() -> Validated<R>
): Validated<R> =
    validation(ValidationContext(this, config))


