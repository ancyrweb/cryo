package fr.cryo.http

class Response(
  val body: String? = null,
  val status: StatusCode = StatusCode.OK,
  val headers: Headers = Headers()
) {
  fun toByteArray(): ByteArray {
    val statusLine = "HTTP/1.1 $status OK\r\n"
    val headers = headers
      .toMap()
      .map { (key, value) -> "$key: $value" }
      .joinToString("\r\n", postfix = "\r\n")

    val body = if (body != null) "$body\r\n" else ""

    return "$statusLine$headers\r\n$body".toByteArray()
  }
}