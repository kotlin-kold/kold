package com.paralainer.kold.validated

sealed class Validated<T> {
    data class Valid<T>(val value: T) : Validated<T>()
    data class Invalid<T>(val violations: List<Violation>) : Validated<T>() {
        fun flatten(fieldNameSeparator: String = ".", arraySuffix: String = "[]"): Set<FlatViolation> =
            violations.flatMap { it.flatten(fieldNameSeparator, arraySuffix) }.toSet()
    }

    inline fun <R> flatMap(block: (T) -> Validated<R>): Validated<R> =
        when (this) {
            is Valid -> block(this.value)
            is Invalid -> Invalid(this.violations)
        }

    inline fun <R> fold(onInvalid: (Invalid<T>) -> R, onValid: (Valid<T>) -> R): R =
        when (this) {
            is Valid -> onValid(this)
            is Invalid -> onInvalid(this)
        }

    fun orNull(): T? = fold({ null }, { it.value })
}

fun <T> T.valid(): Validated<T> = Validated.Valid(this)
fun <T> Violation.invalid(): Validated<T> = Validated.Invalid(listOf(this))