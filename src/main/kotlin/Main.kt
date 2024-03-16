package fr.cryo

import fr.cryo.http.HttpServer


fun main() {
  val server = HttpServer(3909)
  server.run()
}