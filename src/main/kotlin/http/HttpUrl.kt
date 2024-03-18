package fr.cryo.http

class HttpUrl(rawPath: String, val hostname: String, val port: Int) {
  val path: String
  val queryParams: Map<String, String>

  init {
    if (rawPath.contains("?")) {
      val parts = rawPath.split("?")
      val path = parts[0]
      val qs = parts[1]

      val map = HashMap<String, String>()
      qs.split("&").forEach { part ->
        val argParts = part.split("=")
        val key = argParts[0]
        val value = if (argParts.size > 1) argParts[1] else ""

        map[key] = value
      }

      this.path = path
      this.queryParams = map
    } else {
      this.path = rawPath
      this.queryParams = HashMap()
    }
  }
}
