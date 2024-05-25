package me.mgourd.wjsurvey

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.serializer
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.core.serializer.support.SerializationFailedException
import org.springframework.data.repository.CrudRepository

interface SurveyResponseRepository : CrudRepository<SurveyResponse, Long>

@Entity
data class SurveyResponse(
        @ManyToOne var survey: Survey,
        @JdbcTypeCode(SqlTypes.JSON) var responses: List<Answer>,
        @Id @GeneratedValue var id: Long? = null
)

@Serializable data class Answer(var key: String, var answer: AnswerInput)

@Serializable(with = AnswerInputSerializer::class) sealed interface AnswerInput

@Serializable(with = AnswerInputSerializer::class) data class SingleAnswer(var ans: String) : AnswerInput

@Serializable(with = AnswerInputSerializer::class) data class ListAnswer(var ans: List<String>) : AnswerInput

object AnswerInputSerializer : KSerializer<AnswerInput> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AnswerInput") {}

    override fun deserialize(decoder: Decoder): AnswerInput {
        val jsonDecoder =
                decoder as? JsonDecoder
                        ?: throw SerializationFailedException("Only support Json format")
        val obj = jsonDecoder.decodeJsonElement()
        if (obj is JsonArray) {
            return ListAnswer(jsonDecoder.json.decodeFromJsonElement(serializer(), obj))
        } else if (obj is JsonPrimitive) {
            return SingleAnswer(jsonDecoder.json.decodeFromJsonElement(serializer(), obj))
        } else {
            throw SerializationFailedException("Not an array or a string")
        }
    }

    override fun serialize(encoder: Encoder, value: AnswerInput): Unit {
        when (value) {
            is SingleAnswer -> encoder.encodeSerializableValue(serializer(), value.ans)
            is ListAnswer -> encoder.encodeSerializableValue(serializer(), value.ans)
            else -> throw SerializationFailedException("Variation not supported")
        }
    }
}
