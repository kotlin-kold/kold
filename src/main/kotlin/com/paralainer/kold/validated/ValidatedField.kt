package com.paralainer.kold.validated

sealed class ValidatedField<T> {
    fun <R> convertValue(block: (T) -> R): ValidatedField<R> =
        fold(
            onInvalid = { it as InvalidField<R> },
            onValid = { ValidField(it.fieldName, block(it.value)) }
        )

    fun <R> validateValue(validation: (T) -> Validated<R>): ValidatedField<R> =
        fold(
            onValid = {
                validation(it.value).fold(
                    { invalid -> InvalidField<R>(FieldViolation(it.fieldName, invalid.violations)) },
                    { valid -> valid.value.validField(it.fieldName) }
                )
            },
            onInvalid = {
                this as ValidatedField<R> // we are saving a memory allocation by casting instead of reconstructing
            }
        )

    fun <R> fold(onInvalid: (InvalidField<T>) -> R, onValid: (ValidField<T>) -> R): R =
        when (this) {
            is ValidField -> onValid(this)
            is InvalidField -> onInvalid(this)
        }
}

data class ValidField<T>(val fieldName: String, val value: T) : ValidatedField<T>()
data class InvalidField<T>(val violation: FieldViolation) : ValidatedField<T>()

fun <T> Validated<T>.validatedField(fieldName: String): ValidatedField<T> =
    fold(
        onInvalid = {
            it.violations.invalidField(fieldName)
        },
        onValid = {
            it.value.validField(fieldName)
        }
    )

fun <T> T.validField(fieldName: String): ValidField<T> =
    ValidField(fieldName, this)

fun <T> Violation.invalidField(fieldName: String): InvalidField<T> =
    InvalidField(FieldViolation(fieldName, listOf(this)))

fun <T> List<Violation>.invalidField(fieldName: String): InvalidField<T> =
    InvalidField(FieldViolation(fieldName, this))
