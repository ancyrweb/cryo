package fr.cryo.json

class JsonArray(private val list: List<Any>) : JsonElement() {
  fun getString(index: Int): String {
    val value = list[index]
    if (value is String) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getInt(index: Int): Int {
    val value = list[index]
    if (value is Int) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getBoolean(index: Int): Boolean {
    val value = list[index]
    if (value is Boolean) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getObject(index: Int): JsonObject {
    val value = list[index]
    if (value is JsonObject) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getArray(index: Int): JsonArray {
    val value = list[index]
    if (value is JsonArray) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun isNull(index: Int): Boolean {
    val value = list[index]
    return value is JsonNull
  }
}