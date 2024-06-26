package me.mgourd.wjsurvey

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WjsurveyResponseTests {

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

    val response1 = SurveyResponse(survey1, listOf(Answer("name", "John")))
    val response2 = SurveyResponse(survey1, listOf(Answer("age", "1")))

    val response3 = SurveyResponse(survey1, listOf(Answer("name", "John"), Answer("age", "1")))

    val response4 =
            SurveyResponse(
                    survey1,
                    listOf(Answer("name", "John"), Answer("age", "1"), Answer("idk", "idk"))
            )

    @Test
    fun testResponseValidation() {
        // only answering the required question
        assert(survey1.validateResponse(response1))
        // not answering the required question
        assert(!survey1.validateResponse(response2))
        // answering all questions
        assert(survey1.validateResponse(response3))
        // answering a question that doesn't exist
        assert(!survey1.validateResponse(response4))
    }
}
