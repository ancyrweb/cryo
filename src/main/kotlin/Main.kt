package fr.cryo

import fr.cryo.http.HttpMethod
import fr.cryo.http.HttpServer
import fr.cryo.http.routing.DefaultRouter

fun main() {
  val server = HttpServer(3909)
  val router = DefaultRouter()
  router.bind("/", HttpMethod.GET) { request, response ->
    response.setPlainTextBody("Hello world")
  }

  server.use(router)
  server.run()
}