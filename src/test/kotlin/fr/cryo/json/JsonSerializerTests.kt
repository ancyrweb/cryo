package fr.cryo.json

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonSerializerTests {
  fun createObject(str: String): JsonRoot {
    return JsonParser(str).parse()
  }

  fun serialize(root: JsonRoot): String {
    return JsonSerializer().serialize(root)
  }

  @Nested
  inner class SingleValues

  @Test
  fun `string`() {
    val str = """{"key":"value"}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `boolean`() {
    val str = """{"key":true}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `integer`() {
    val str = """{"key":100}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `decimal`() {
    val str = """{"key":100.0}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `object`() {
    val str = """{"key":{"a":1,"b":true}}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `null`() {
    val str = """{"key":null}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `array`() {
    val str = """{"key":[1,true,10.0,"ok",null]}"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }

  @Test
  fun `root array`() {
    val str = """[1,true,10.0,"ok",null,{"foo":"bar"}]"""
    assertEquals(str, JsonSerializer().serialize(createObject(str)))
  }
}