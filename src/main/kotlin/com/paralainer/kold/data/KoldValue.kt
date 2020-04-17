package com.paralainer.kold.data

import com.paralainer.kold.validated.Validated
import com.paralainer.kold.validated.ValueViolation
import com.paralainer.kold.validated.invalid
import com.paralainer.kold.validated.valid

class KoldValue private constructor(private val value: Any) {
    fun string(onInvalid: () -> ValueViolation): Validated<String> =
        (value as? String)?.valid() ?: onInvalid().invalid()

    fun bool(onInvalid: () -> ValueViolation): Validated<Boolean> =
        (value as? Boolean)?.valid() ?: onInvalid().invalid()

    fun number(onInvalid: () -> ValueViolation): Validated<Number> =
        (value as? Number)?.valid() ?: onInvalid().invalid()

    fun list(onInvalid: () -> ValueViolation): Validated<List<KoldValue?>> =
        (value as? List<KoldValue?>)?.valid() ?: onInvalid().invalid()

    fun obj(onInvalid: () -> ValueViolation): Validated<KoldData> =
        (value as? KoldData)?.valid() ?: onInvalid().invalid()


    companion object {
        fun fromString(s: String) = KoldValue(s)
        fun fromNumber(n: Number) = KoldValue(n)
        fun fromCollection(c: Collection<KoldValue?>) =
            KoldValue(c.toList())

        fun fromObject(data: KoldData) = KoldValue(data)
    }
}