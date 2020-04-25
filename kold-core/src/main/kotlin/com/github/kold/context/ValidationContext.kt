package com.github.kold.context

import com.github.kold.data.KoldData
import com.github.kold.data.KoldValue
import com.github.kold.validated.*

class ValidationContext(
    val data: KoldData,
    val config: ValidationContextConfig
) {
    fun require(fieldName: String): ValidatedField<KoldValue> =
        data[fieldName]?.validField(fieldName) ?: config.nullValue.invalidField(fieldName)

    fun opt(fieldName: String): OptionalField =
        OptionalField(fieldName, data[fieldName])

    fun Validated<KoldValue>.string(): Validated<String> = this.flatMap { it.string() }
    fun KoldValue.string(): Validated<String> = this.string { config.invalidValue }

    fun Validated<KoldValue>.bool(): Validated<Boolean> = this.flatMap { it.bool() }
    fun KoldValue.bool(): Validated<Boolean> = this.bool { config.invalidValue }

    fun Validated<KoldValue>.int(): Validated<Int> = this.flatMap { it.int() }
    fun KoldValue.int(): Validated<Int> =
        this.number { config.invalidValue }.flatMap { it.safeToInt() }

    fun Validated<KoldValue>.long(): Validated<Long> = this.flatMap { it.long() }
    fun KoldValue.long(): Validated<Long> =
        this.number { config.invalidValue }.flatMap { it.safeToLong() }

    fun Validated<KoldValue>.double(): Validated<Double> = this.flatMap { it.double() }
    fun KoldValue.double(): Validated<Double> =
        this.number { config.invalidValue }.flatMap { it.toDouble().valid() }

    fun Validated<KoldValue>.list(): Validated<List<KoldValue?>> = this.flatMap { it.list() }
    fun KoldValue.list(): Validated<List<KoldValue?>> = this.list { config.invalidValue }

    fun Validated<KoldValue>.obj(): Validated<KoldData> = flatMap { it.obj() }
    fun KoldValue.obj(): Validated<KoldData> = this.obj { config.invalidValue }

    fun <R> R?.notNull(): Validated<R> = this?.valid() ?: config.nullValue.invalid()

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

fun <R> KoldData.validationContext(
    config: ValidationContextConfig = ValidationContextConfig(),
    validation: ValidationContext.() -> Validated<R>
): Validated<R> =
    validation(ValidationContext(this, config))

fun <T> ValidatedField<T?>.default(default: T): ValidatedField<T> =
    this.convertValue { it ?: default }

fun <T, R> T?.validateOption(block: (T) -> Validated<R>): Validated<R?> =
    if (this == null)
        Validated.Valid(null)
    else
        block(this).flatMap { Validated.Valid(it as R?) }

fun <T, R> Validated<List<T>>.validateElements(block: (T) -> Validated<R>): Validated<List<R>> =
    this.flatMap { list ->
        val validatedElements = list.map { block(it) }
        val violations = collectViolations(validatedElements)

        if (violations.isEmpty()) {
            validatedElements.map { (it as Validated.Valid<R>).value }.valid()
        } else {
            ElementsViolation(violations.toList()).invalid()
        }
    }


private fun <R> collectViolations(validatedElements: List<Validated<R>>): Set<Violation> {
    return validatedElements.mapNotNull {
        when (it) {
            is Validated.Valid -> null
            is Validated.Invalid -> it.violations
        }
    }.flatten().toSet()
}


