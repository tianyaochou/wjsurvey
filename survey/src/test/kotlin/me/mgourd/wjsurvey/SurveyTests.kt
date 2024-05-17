package me.mgourd.wjsurvey

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SurveyTests {

    val survey1 =
            Survey(
                    "test",
                    listOf(
                            Text("Example"),
                            Question("name", "What is your name", true, TextInput("")),
                            Question(
                                    "age",
                                    "How old are you?",
                                    false,
                                    SingleChoice(listOf(Choice("1", "< 18"), Choice("2", ">= 18")))
                            )
                    )
            )

    @Test
    fun testValidation1() {

        assert(survey1.validateQuestionKeys())
    }

    val survey2 = Survey("test", listOf(Text("Example")))

    @Test
    fun testSerialization1() {
        val list = listOf(survey2, survey1)
        val json = Json.encodeToString(listOf(survey2, survey1))
        val survey_de = Json.decodeFromString<List<Survey>>(json)
        assertEquals(list, survey_de)
    }

    @Test
    fun testScaleSerialization() {
        val scale: Input = Scale(7);
        assertEquals(Json.encodeToString(scale), """{"type":"Scale","points":7}""")
    }
}
