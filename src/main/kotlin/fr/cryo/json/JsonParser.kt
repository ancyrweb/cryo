package fr.cryo.json

class JsonParser(data: String) {
  private val json = data.trim()
  private var current = 0
  private val builder = StringBuilder()

  private fun trim() {
    while (
      json[current].isWhitespace() ||
      json[current] == '\n' ||
      json[current] == '\t'
    ) {
      current++
    }
  }

  private fun consume(c: Char) {
    if (json[current] != c) {
      throw RuntimeException("Invalid JSON")
    }

    current++
  }

  private fun peek(): Char {
    return json[current]
  }

  private fun advance(): Char {
    current++
    return json[current - 1]
  }

  private fun eof(): Boolean {
    return current == json.length;
  }

  fun parse(): JsonRoot {
    val obj = parseValue()

    if (!eof()) {
      throw RuntimeException("Invalid JSON : expected EOF")
    }

    return JsonRoot(obj)
  }

  private fun parseObj(): JsonObject {
    consume('{')

    val map = HashMap<String, Any>()

    while (true) {
      trim()
      consume('"')

      builder.clear()
      while (peek() != '"') {
        builder.append(advance())
      }

      val key = builder.toString()

      consume('"')
      trim()
      consume(':')
      trim()

      map[key] = parseValue()

      trim()
      if (peek() == '}') {
        consume('}')
        break
      }

      consume(',')
      trim()
    }

    return JsonObject(map)
  }

  private fun parseArray(): JsonArray {
    consume('[')

    val items = ArrayList<Any>()

    while (true) {
      trim()

      val value = parseValue()
      items.add(value)

      trim()
      if (peek() == ']') {
        consume(']')
        break
      }

      consume(',')
    }

    return JsonArray(items)
  }

  private fun parseValue(): Any {
    when (peek()) {
      '"' -> return parseString()
      '{' -> return parseObj()
      '[' -> return parseArray()
      else -> {
        if (peek().isDigit()) {
          return parseInt()
        } else if (peek().isLetter()) {
          return parseKeyword()
        }

        throw RuntimeException("Invalid JSON")
      }
    }
  }

  private fun parseKeyword(): Any {
    builder.clear()
    while (peek().isLetter()) {
      builder.append(advance())
    }

    val value = builder.toString()
    if (value == "true" || value == "false") {
      return value == "true"
    } else if (value == "null") {
      return JsonNull()
    }

    throw RuntimeException("Invalid JSON")
  }

  private fun parseInt(): Int {
    var number = 0

    while (peek().isDigit()) {
      number *= 10
      number += advance().digitToInt()
    }

    return number
  }

  private fun parseString(): String {
    consume('"')

    builder.clear()
    while (peek() != '"') {
      builder.append(advance())
    }

    consume('"')
    return builder.toString()
  }
}