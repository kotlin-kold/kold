package com.paralainer.kold.validated

import com.paralainer.kold.data.KoldValue

data class OptionalField(val fieldName: String, private val value: KoldValue?) {
    fun <R> validateOption(block: (KoldValue) -> Validated<R>): ValidatedField<R?> =
        ValidatedField(
            fieldName,
            if (value == null) {
                null.valid<R?>()
            } else {
                block(value) as Validated<R?>
            }
        )
}
