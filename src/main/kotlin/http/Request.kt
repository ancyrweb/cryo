package fr.cryo.http

import java.io.InputStream

class Request(input: InputStream) {
  lateinit var method: HttpMethod
    private set

  var uri: String = ""
    private set

  var version: String = ""
    private set

  var headers = Headers()
    private set

  var body: String = ""
    private set

  init {
    readRequestLine(input)
    readHeaders(input)

    if (headers.has("Content-Length")) {
      val contentLength = headers.get("Content-Length")!!.toInt()
      this.body = readTokens(input, -1, contentLength).trim()
    }
  }

  private fun readRequestLine(input: InputStream) {
    val line = readTokens(input, '\n'.code, 8192)
    val parts = line.split(" ")

    this.method = HttpMethod.fromString(parts[0].trim())
    this.uri = parts[1].trim()
    this.version = parts[2].trim()
  }

  private fun readHeaders(input: InputStream) {
    while (true) {
      val line = readLine(input)
      if (line.isEmpty()) {
        break
      }

      val separatorIndex = line.indexOf(":")
      if (separatorIndex == -1) {
        throw MalformattedRequestException("Invalid header line: $line")
      }

      val parts = line.split(":")
      val key = parts[0].trim()
      val value = parts[1].trim()

      headers.set(key, value)
    }
  }

  private fun readLine(input: InputStream): String {
    val line = readTokens(input, '\n'.code, 8192)
    if (line.endsWith("\r")) {
      return line.substring(0, line.length - 1)
    }

    return line
  }

  private fun readTokens(
    input: InputStream,
    delimiter: Int,
    maxLength: Int
  ): String {
    var buffer = ByteArray(512)
    var idx = 0

    while (input.available() > 0 && idx < maxLength) {
      val c = input.read()
      if (c == -1 || c == delimiter) {
        break
      }

      if (buffer.size == idx) {
        val newBuffer = ByteArray(buffer.size * 2)
        System.arraycopy(
          buffer,
          0,
          newBuffer,
          0,
          buffer.size
        )

        buffer = newBuffer
      }

      buffer[idx++] = c.toByte()
    }

    return String(buffer, 0, idx)
  }

  fun toStringSummary(): String {
    return "$method $uri"
  }

  class MalformattedRequestException(message: String) : Exception(message)
}