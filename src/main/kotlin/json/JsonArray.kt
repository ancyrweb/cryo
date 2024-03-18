package fr.cryo.json

class JsonArray(private val list: MutableList<Any> = ArrayList()) :
  JsonElement() {
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

  fun forEach(handler: (value: Any) -> Unit) {
    list.forEach(handler)
  }

  fun add(value: String) = this.list.add(value)
  fun add(value: Int) = this.list.add(value)
  fun add(value: Double) = this.list.add(value)
  fun add(value: Boolean) = this.list.add(value)
  fun add(value: JsonObject) = this.list.add(value)
  fun add(value: JsonArray) = this.list.add(value)

  fun addAt(index: Int, value: String) = this.list.add(index, value)
  fun addAt(index: Int, value: Int) = this.list.add(index, value)
  fun addAt(index: Int, value: Double) = this.list.add(index, value)
  fun addAt(index: Int, value: Boolean) = this.list.add(index, value)
  fun addAt(index: Int, value: JsonObject) = this.list.add(index, value)
  fun addAt(index: Int, value: JsonArray) = this.list.add(index, value)
  fun addNullAt(index: Int) = this.list.add(index, JsonNull())
}