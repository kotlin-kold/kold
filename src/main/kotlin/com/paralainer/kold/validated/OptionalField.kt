package com.paralainer.kold.validated

import com.paralainer.kold.data.KoldValue

data class OptionalField(val fieldName: String, private val value: KoldValue?) {
    fun <R> validateOption(block: (KoldValue) -> Validated<R>): ValidatedField<R?> =
            if (value == null) {
                ValidField(fieldName, null)
            } else {
                block(value).validatedField(fieldName) as ValidatedField<R?>
            }
}
