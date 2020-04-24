package com.paralainer.kold.data

import com.paralainer.kold.validated.Validated
import com.paralainer.kold.validated.ValueViolation
import com.paralainer.kold.validated.invalid
import com.paralainer.kold.validated.valid

class KoldValue private constructor(internal val value: Any) {
    companion object {
        fun fromString(s: String) = KoldValue(s)
        fun fromNumber(n: Number) = KoldValue(n)
        fun fromBoolean(b: Boolean) = KoldValue(b)
        fun fromCollection(c: Collection<KoldValue?>) =
            KoldValue(c.toList())

        fun fromObject(data: KoldData) = KoldValue(data)
    }

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KoldValue) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }


}
