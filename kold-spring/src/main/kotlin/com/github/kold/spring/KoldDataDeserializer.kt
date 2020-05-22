package com.github.kold.spring

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.kold.data.KoldData
import com.github.kold.data.KoldDataParsingException
import com.github.kold.data.KoldValue
import org.springframework.stereotype.Component

@Component
class KoldDataDeserializer : JsonDeserializer<KoldData>() {
    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): KoldData {
        val jsonNode = jsonParser.codec.readTree<JsonNode>(jsonParser)
        return parseObject(jsonNode as ObjectNode)
    }

    private fun asKoldValue(fieldName: String, value: JsonNode): KoldValue? =
        when {
            value.isBoolean -> KoldValue.fromBoolean(value.booleanValue())

            value.isTextual -> KoldValue.fromString(value.textValue())

            value.isLong -> KoldValue.fromNumber(value.longValue())
            value.isInt -> KoldValue.fromNumber(value.intValue())
            value.isShort -> KoldValue.fromNumber(value.shortValue())

            value.isFloat -> KoldValue.fromNumber(value.floatValue())
            value.isDouble -> KoldValue.fromNumber(value.doubleValue())

            value.isArray -> parseArray(fieldName, value as ArrayNode)
            value.isObject -> KoldValue.fromObject(parseObject(value as ObjectNode))

            value.isNull -> null
            else ->
                throw KoldDataParsingException("Could not parse ${fieldName}, unknown type")
        }

    private fun parseObject(json: ObjectNode): KoldData =
        json.fields().asSequence().map { field ->
            field.key to asKoldValue(field.key, field.value)
        }.toMap().let(::KoldData)

    private fun parseArray(fieldName: String, array: ArrayNode): KoldValue =
        KoldValue.fromCollection(
            array.iterator().asSequence().map {
                asKoldValue(fieldName, it)
            }.toList()
        )

}
