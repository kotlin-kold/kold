package com.paralainer.kold.data

class KoldData(private val data: Map<String, KoldValue?>) {
    operator fun get(fieldName: String): KoldValue? = data[fieldName]
    fun contains(fieldName: String): Boolean = data.containsKey(fieldName)

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
                is Collection<*> -> KoldValue.fromCollection(rawValue.map {
                    parseValue(
                        it
                    )
                })
                is Map<*, *> -> KoldValue.fromObject(parseMap(rawValue))
                is Number -> KoldValue.fromNumber(rawValue)
                is String -> KoldValue.fromString(rawValue)
                else ->
                    throw KoldDataParsingException("Value of unexpected type ${rawValue::class} found")
            }
    }
}

class KoldDataParsingException(message: String) : Exception(message)
