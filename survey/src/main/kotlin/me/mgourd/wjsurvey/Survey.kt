package me.mgourd.wjsurvey

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.GenerationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.repository.CrudRepository

interface SurveyRepository : CrudRepository<Survey, Long>

@Entity
@Serializable
data class Survey(
        @Column var name: String = "",
        @Column @JdbcTypeCode(SqlTypes.JSON) var content: List<SurveyContent> = listOf(),
        @Transient @OneToMany(mappedBy = "survey") var responses: List<SurveyResponse> = listOf(),
        @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "survey_id_seq") @SequenceGenerator(name="survey_id_seq", sequenceName="survey_seq", allocationSize=1) var id: Long? = null
) {
    // Two questions should not have the same key
    fun validateQuestionKeys(): Boolean {
        val set = mutableSetOf<String>()
        for (c in this.content) {
            if (c is Question) {
                if (set.contains(c.key)) {
                    return false
                }
                set.add(c.key)
            }
        }
        return true
    }

    // Validate that answers in response appear in survey
    fun validateResponse(resp: SurveyResponse): Boolean {
        val questionKeys: Set<String> = this.content.filter({ it is Question }).map({ (it as Question).key }).toSet()
        val requiredKeys: Set<String> = this.content.filter({ it is Question && it.required }).map({ (it as Question).key }).toSet()
        if (resp.responses.all({ it.key in questionKeys })){
            if (!requiredKeys.all({ it in resp.responses.map({ it.key }) })){
                // Not all required questions are answered
                return false
            }

            return true
        } else {
            return false
        }
    }
}

@Serializable
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
)
@JsonSubTypes(
        JsonSubTypes.Type(value = Text::class, name = "Text"),
        JsonSubTypes.Type(value = PageBreak::class, name = "PageBreak"),
) // Hibernate is using jakson instead of kotlinx.serialization, therefore not using @SerialName
sealed interface SurveyContent

@Serializable @SerialName("Text") data class Text(var content: String) : SurveyContent

@Serializable @SerialName("PageBreak") object PageBreak : SurveyContent

@Serializable
@SerialName("Question")
data class Question(
        var key: String,
        var question: String,
        var required: Boolean,
        var input: Input,
) : SurveyContent

// ----------------- Question Input Types-------------------

@Serializable
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
)
@JsonSubTypes(
        JsonSubTypes.Type(value = SingleChoice::class, name = "SingleChoice"),
        JsonSubTypes.Type(value = MultiChoice::class, name = "MultiChoice"),
        JsonSubTypes.Type(value = Scale::class, name = "Scale"),
        JsonSubTypes.Type(value = TextInput::class, name = "TextInput"),
        JsonSubTypes.Type(value = LongTextInput::class, name = "LongTextInput"),
) // Hibernate is using jakson instead of kotlinx.serialization, therefore not using @SerialName
sealed interface Input

@Serializable data class Choice(var encoding: String, var description: String)

@Serializable
@SerialName("SingleChoice")
@JsonTypeName("SingleChoice")
data class SingleChoice(var choices: List<Choice>) : Input

@Serializable
@SerialName("MultiChoice")
@JsonTypeName("MultiChoice")
data class MultiChoice(var choices: List<Choice>) : Input

@Serializable @SerialName("Scale") @JsonTypeName("Scale") data class Scale(var points: Int) : Input

@Serializable
@SerialName("TextInput")
@JsonTypeName("TextInput")
data class TextInput(var prompt: String) : Input

@Serializable
@SerialName("LongTextInput")
@JsonTypeName("LongTextInput")
data class LongTextInput(var prompt: String) : Input
