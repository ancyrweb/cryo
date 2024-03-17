package fr.cryo.json

class JsonSerializer {
  private var builder = StringBuilder()

  fun serialize(root: JsonRoot): String {
    builder.clear()

    if (root.isArray()) {
      serializeArray(root.asArray())
    } else {
      serializeObject(root.asObject())
    }

    return builder.toString()
  }

  private fun serializeArray(array: JsonArray) {
    append('[')

    array.forEach { value ->
      serializeValue(value)
      append(",")
    }

    removeTrailingComma()
    append(']')
  }

  private fun serializeObject(obj: JsonObject) {
    append('{')

    obj.forEach { key, value ->
      append(""""$key":""")
      serializeValue(value)
      append(",")
    }

    removeTrailingComma()
    append('}')
  }

  private fun serializeValue(value: Any) {
    when (value) {
      is String -> append("\"$value\"")
      is JsonArray -> serializeArray(value)
      is JsonObject -> serializeObject(value)
      is JsonNull -> append("null")
      else -> append(value.toString())
    }
  }

  private fun append(c: Char) {
    builder.append(c)
  }

  private fun append(c: String) {
    builder.append(c)
  }

  private fun removeTrailingComma() {
    if (builder.last() == ',') {
      builder.deleteCharAt(builder.length - 1)
    }
  }
}