package com.github.kold.data

import com.github.kold.validated.*

class KoldData(internal val data: Map<String, KoldValue?>) {
    operator fun get(fieldName: String): KoldValue? = data[fieldName]
    fun contains(fieldName: String): Boolean = data.containsKey(fieldName)

    fun <V> toMap(validateValue: (String, KoldValue?) -> Validated<V>): Validated<Map<String, V>> {
        val validatedMap = data.mapValues { (key, value) ->
            validateValue(key, value)
        }

        val violations = collectViolations(validatedMap.values)

        return if (violations.isEmpty()) {
            validatedMap.mapValues { (it.value as Validated.Valid).value }.valid()
        } else {
            ElementsViolation(violations).invalid()
        }
    }

    private fun <V> collectViolations(validations: Collection<Validated<V>>): List<Violation> =
        validations.mapNotNull { field ->
            field.fold(
                onInvalid = { it.violations },
                onValid = { null }
            )
        }.flatten()

    companion object {
        fun fromMap(map: Map<String, Any?>): KoldData =
            parseMap(map)

        private fun parseMap(map: Map<*, *>): KoldData =
            map.map { (key, rawValue) ->
                if (key !is String) {
                    throw KoldDataParsingException("Only keys of type string are supported")
                }

                val value = parseValue(rawValue)

                key to value
            }.toMap().let(::KoldData)

        private fun parseValue(rawValue: Any?): KoldValue? =
            when (rawValue) {
                null -> null
                is Collection<*> -> KoldValue.fromCollection(rawValue.map { parseValue(it) })
                is Map<*, *> -> KoldValue.fromObject(parseMap(rawValue))
                is Number -> KoldValue.fromNumber(rawValue)
                is String -> KoldValue.fromString(rawValue)
                is Boolean -> KoldValue.fromBoolean(rawValue)
                else ->
                    throw KoldDataParsingException("Value of unexpected type ${rawValue::class} found")
            }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KoldData) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

class KoldDataParsingException(message: String) : Exception(message)
