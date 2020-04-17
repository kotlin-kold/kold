package com.paralainer.kold.validated

import com.paralainer.kold.data.KoldValue

data class ValidatedField<T>(val fieldName: String, val value: Validated<T>) {
    fun <R> convertValue(block: (T) -> R): ValidatedField<R> =
        validateValue { block(it).valid() }

    fun <R> validateValue(validation: (T) -> Validated<R>): ValidatedField<R> =
        value.fold(
            onValid = {
                val validated = validation(it.value).fold(
                    { invalid -> FieldViolation(fieldName, invalid.violations).invalid<R>() },
                    { valid -> valid.value.valid() }
                )

                ValidatedField(fieldName, validated)
            },
            onInvalid = {
                ValidatedField(fieldName, Validated.Invalid(it.violations))
            }
        )
}

fun <T> T.validField(fieldName: String): ValidatedField<T> =
    ValidatedField(fieldName, this.valid())

fun <T> ValueViolation.invalidField(fieldName: String): ValidatedField<T> =
    ValidatedField(fieldName, Validated.Invalid(listOf(FieldViolation(fieldName, listOf(this)))))

data class OptionalField(val fieldName: String, private val value: KoldValue?) {
    fun <R> validateOption(block: (KoldValue) -> Validated<R>): ValidatedField<R?> =
        if (value == null) {
            null.validField(fieldName)
        } else {
            value.validField(fieldName).validateValue(block).convertValue { it as R? }
        }
}