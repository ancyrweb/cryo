package fr.cryo.json

class JsonObject(private val map: MutableMap<String, Any> = HashMap()) :
  JsonElement() {
  fun getString(key: String): String {
    val value = map[key]
    if (value is String) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getInt(key: String): Int {
    val value = map[key]
    if (value is Int) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getDouble(key: String): Double {
    val value = map[key]
    if (value is Double) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getBoolean(key: String): Boolean {
    val value = map[key]
    if (value is Boolean) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getObject(key: String): JsonObject {
    val value = map[key]
    if (value is JsonObject) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun getArray(key: String): JsonArray {
    val value = map[key]
    if (value is JsonArray) {
      return value
    }

    throw RuntimeException("Invalid type")
  }

  fun isNull(key: String): Boolean {
    val value = map[key]
    return value is JsonNull
  }

  fun forEach(handler: (key: String, value: Any) -> Unit) = map.forEach(handler)

  fun put(key: String, value: String) = this.map.set(key, value)
  fun put(key: String, value: Int) = this.map.set(key, value)
  fun put(key: String, value: Double) = this.map.set(key, value)
  fun put(key: String, value: Boolean) = this.map.set(key, value)
  fun put(key: String, value: JsonObject) = this.map.set(key, value)
  fun put(key: String, value: JsonArray) = this.map.set(key, value)
  fun put(key: String) = this.map.set(key, JsonNull())
}