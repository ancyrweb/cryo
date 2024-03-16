package fr.cryo

import fr.cryo.http.*

fun main() {
  val server = HttpServer(3909)
  val router = Router()
  router.bind("/", HttpMethod.GET) {
    Response(
      body = "Hello, World!",
      headers = Headers().apply {
        add("Content-Type", "text/plain")
      },
    )
  }

  server.use(router)
  server.run()
}