package fr.cryo.http

class Headers(
  private val headers: MutableMap<String, String> = mutableMapOf()
) {

  fun set(key: String, value: String) {
    headers[key] = value
  }

  fun set(pair: Pair<String, String>) {
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

  override fun toString(): String {
    val builder = StringBuilder()
    builder.append("Headers(\n");
    headers.entries.forEach { entry ->
      builder.append("\t" + entry.key + " : " + entry.value + "\n")
    }

    builder.append(")")

    return builder.toString()
  }
}