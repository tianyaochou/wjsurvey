package me.mgourd.wjsurvey

import kotlin.test.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.serializer.support.SerializationFailedException

class ResponseTests {
    val answer1 = Answer("age", SingleAnswer("1"))
    val json11 = """{"key":"age","answer":"1"}"""
    val json12 = """{"key":"age","answer":1}"""

    @Test
    fun serializeTest1() {
        assertEquals(Json.encodeToString(answer1), json11)
    }

    @Test
    fun deserializeTest1() {
        assertThrows<Throwable> { Json.decodeFromString(json12) }
    }

    val answer2 = Answer("fruit", ListAnswer(listOf("apple", "banana")))
    val json2 = """{"key":"fruit","answer":["apple","banana"]}"""

    @Test
    fun seAndDeTest() {
        assertEquals(Json.encodeToString(answer2), json2)
        assertEquals(Json.decodeFromString(json2), answer2)
    }
}
