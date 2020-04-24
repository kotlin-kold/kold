package com.paralainer.kold.validated

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
                this as ValidatedField<R> // we are saving a memory allocation by casting instead of reconstructing
            }
        )
}

fun <T> T.validField(fieldName: String): ValidatedField<T> =
    ValidatedField(fieldName, this.valid())

fun <T> ValueViolation.invalidField(fieldName: String): ValidatedField<T> =
    ValidatedField(fieldName, Validated.Invalid(listOf(FieldViolation(fieldName, listOf(this)))))
