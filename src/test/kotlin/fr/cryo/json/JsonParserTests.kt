package fr.cryo.json

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonParserTests {
  @Nested
  inner class SingleValues {
    @Test
    fun `parse a simple string`() {
      val obj = JsonParser("""{"key":"value"}""")
        .parse()
        .asObject()

      assertEquals("value", obj.getString("key"))
    }

    @Test
    fun `parse a simple string with spaces`() {
      val obj =
        JsonParser("""{"key":"value"}""")
          .parse()
          .asObject()

      assertEquals("value", obj.getString("key"))
    }

    @Test
    fun `parse an integer`() {
      val obj = JsonParser("""{"key":123}""")
        .parse()
        .asObject()

      assertEquals(123, obj.getInt("key"))
    }

    @Test
    fun `parse a true boolean`() {
      val obj = JsonParser("""{"key":true}""")
        .parse()
        .asObject()

      assertEquals(true, obj.getBoolean("key"))
    }

    @Test
    fun `parse a false boolean`() {
      val obj = JsonParser("""{"key":false}""")
        .parse()
        .asObject()

      assertEquals(false, obj.getBoolean("key"))
    }

    @Test
    fun `parse a simple nested object`() {
      val obj =
        JsonParser("""{"name":{"first":"Anthony", "second": "Cyrille"}}""")
          .parse()
          .asObject()

      val name = obj.getObject("name")

      assertEquals("Anthony", name.getString("first"))
      assertEquals("Cyrille", name.getString("second"))
    }

    @Test
    fun `parse deeply nested object`() {
      val obj =
        JsonParser("""{"a": {"b": {"c": {"d": true }}}}""")
          .parse()
          .asObject()

      val result = obj.getObject("a")
        .getObject("b")
        .getObject("c")
        .getBoolean("d")

      assertEquals(true, result)
    }

    @Test
    fun `parse a root array`() {
      val obj = JsonParser("""[1, true, "three"]""")
        .parse()
        .asArray()

      assertEquals(1, obj.getInt(0))
      assertEquals(true, obj.getBoolean(1))
      assertEquals("three", obj.getString(2))
    }

    @Test
    fun `parse a nested array`() {
      val obj = JsonParser("""{"key":[1, true, "three"]}""")
        .parse()
        .asObject()

      assertEquals(1, obj.getArray("key").getInt(0))
      assertEquals(true, obj.getArray("key").getBoolean(1))
      assertEquals("three", obj.getArray("key").getString(2))
    }

    @Test
    fun `parse a null value`() {
      val obj =
        JsonParser("""{"name": null}""")
          .parse()
          .asObject()

      assertEquals(true, obj.isNull("name"))
    }
  }

  @Nested
  inner class MultipleValues {
    @Test
    fun `parse a simple object`() {
      val obj =
        JsonParser("""{"name":"toto","age":23,"rich":false}""")
          .parse()
          .asObject()

      assertEquals("toto", obj.getString("name"))
      assertEquals(23, obj.getInt("age"))
      assertEquals(false, obj.getBoolean("rich"))
    }
  }

  @Nested
  inner class DifferentTemplateTests {
    @Test
    fun `parse an object with many spaces`() {
      val obj =
        JsonParser(
          """     {   "name" :  {"first"  : "Anthony"  , "second":  "Cyrille" }  }   """
        )
          .parse()
          .asObject()

      val name = obj.getObject("name")

      assertEquals("Anthony", name.getString("first"))
      assertEquals("Cyrille", name.getString("second"))
    }

    @Test
    fun `parse an object with line breaks, spaces and tabs`() {
      val obj =
        JsonParser(
          """
            {   
               "name": {
                    "first" :   "Anthony", 
                    "second":   "Cyrille",
                    "age":      29,
                    "senior":   false,
                    "items":    [1, "two", true, null],
                    "lastJob":  null
                }
            }   
         """
        )
          .parse()
          .asObject()

      val name = obj.getObject("name")

      assertEquals("Anthony", name.getString("first"))
      assertEquals("Cyrille", name.getString("second"))
      assertEquals(29, name.getInt("age"))
      assertEquals(false, name.getBoolean("senior"))

      val items = name.getArray("items");
      assertEquals(1, items.getInt(0))
      assertEquals("two", items.getString(1))
      assertEquals(true, items.getBoolean(2))
      assertEquals(true, items.isNull(3))

      assertEquals(true, name.isNull("lastJob"))
    }
  }

}