package com.github.kold.validated

sealed class Violation {
    fun flatten(fieldNameSeparator: String = ".", arraySuffix: String = "[]"): Set<FlatViolation> =
        flattenInternal(fieldNameSeparator, arraySuffix, null).toSet()

    private fun flattenInternal(
        fieldNameSeparator: String,
        arraySuffix: String,
        parentFieldName: String?
    ): List<FlatViolation> =
        when (this) {
            is ElementsViolation -> violations.flatMap {
                it.flattenInternal(fieldNameSeparator, arraySuffix, fieldName(parentFieldName, fieldNameSeparator, arraySuffix))
            }
            is FieldViolation -> violations.flatMap {
                it.flattenInternal(fieldNameSeparator, arraySuffix, fieldName(parentFieldName, fieldNameSeparator, fieldName))
            }
            is ValueViolation -> listOf(
                FlatViolation(
                    parentFieldName, errorCode, description
                )
            )
        }

    private fun fieldName(parentFieldName: String?, fieldNameSeparator: String, fieldName: String) =
        if (parentFieldName != null) {
            parentFieldName + fieldNameSeparator + fieldName
        } else {
            fieldName
        }

}

data class ElementsViolation(val violations: List<Violation>) : Violation()
data class FieldViolation(val fieldName: String, val violations: List<Violation>) : Violation()
data class ValueViolation(val errorCode: String, val description: String? = null) : Violation()

data class FlatViolation(val fieldName: String?, val errorCode: String, val description: String?)