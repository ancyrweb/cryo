package fr.cryo.http

enum class HttpMethod {
  GET,
  HEAD,
  OPTIONS,
  CONNECT,
  POST,
  PATCH,
  PUT,
  DELETE,
  TRACE;

  companion object {
    fun fromString(value: String): HttpMethod {
      return when (value.uppercase()) {
        "GET" -> GET
        "HEAD" -> HEAD
        "OPTIONS" -> OPTIONS
        "CONNECT" -> CONNECT
        "POST" -> POST
        "PATCH" -> PATCH
        "PUT" -> PUT
        "DELETE" -> DELETE
        "TRACE" -> TRACE
        else -> throw RuntimeException("Unrecognized method " + value)
      }
    }
  }
}