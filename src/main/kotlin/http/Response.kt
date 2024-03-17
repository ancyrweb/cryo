package fr.cryo.http

import fr.cryo.json.JsonRoot
import fr.cryo.json.JsonSerializer

class Response(
  body: String? = null,
  status: StatusCode = StatusCode.OK,
  headers: Headers = Headers()
) {
  var body: String? = body
    private set

  var status: StatusCode = status
    private set

  var headers: Headers = headers
    private set

  fun respondText(body: String) {
    this.body = body
    this.headers.set("Content-Type", "text/plain")
  }

  fun respondJson(root: JsonRoot) {
    this.body = JsonSerializer().serialize(root)
    this.headers.set("Content-Type", "application/json")
  }

  fun setStatusCode(status: StatusCode) {
    this.status = status
  }

  fun toByteArray(): ByteArray {
    return toString().toByteArray()
  }

  fun isSuccess(): Boolean {
    return this.status.code < 400
  }

  override fun toString(): String {
    val statusLine = getStatusLine() + "\r\n"
    val headers = headers
      .toMap()
      .map { (key, value) -> "$key: $value" }
      .joinToString("\r\n", postfix = "\r\n")

    val body = if (body != null) "$body\r\n" else ""

    return "$statusLine$headers\r\n$body"
  }

  fun toStringSummary(): String {
    return getStatusLine()
  }

  private fun getStatusLine(): String {
    return "HTTP/1.1 $status"
  }
}