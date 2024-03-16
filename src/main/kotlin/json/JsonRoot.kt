package fr.cryo.json

class JsonRoot(private val root: Any) {
  fun asObject(): JsonObject {
    if (root is JsonObject) {
      return root
    }

    throw RuntimeException("Not an object")
  }

  fun asArray(): JsonArray {
    if (root is JsonArray) {
      return root
    }

    throw RuntimeException("Not an object")
  }
}