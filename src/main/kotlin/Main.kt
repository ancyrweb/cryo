package fr.cryo

import fr.cryo.http.HttpMethod
import fr.cryo.http.HttpServer
import fr.cryo.http.routing.DefaultRouter
import fr.cryo.json.JsonParser

fun main() {
  val server = HttpServer(3909)
  val router = DefaultRouter()

  router.bind("/", HttpMethod.GET) { request, response ->
    response.respondJson(
      JsonParser("""{"version":1}""").parse()
    )
  }

  router.bind("/", HttpMethod.POST) { request, response ->
    response.respondJson(
      JsonParser("""{"result":"OK!"}""").parse()
    )
  }

  server.use(router)
  server.run()
}