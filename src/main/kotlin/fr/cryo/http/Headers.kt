package fr.cryo.http

class Headers(
  private val headers: MutableMap<String, String> = mutableMapOf()
) {

  fun add(key: String, value: String) {
    headers[key] = value
  }

  fun add(pair: Pair<String, String>) {
    headers[pair.first] = pair.second
  }

  fun get(key: String): String? {
    return headers[key]
  }

  fun has(key: String): Boolean {
    return headers.containsKey(key)
  }

  fun remove(key: String) {
    headers.remove(key)
  }

  fun clear() {
    headers.clear()
  }

  fun toMap(): Map<String, String> {
    return headers.toMap()
  }
}