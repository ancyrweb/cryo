package fr.cryo.http

import java.util.*

class Router {
  private val map = EnumMap<
      HttpMethod,
      ArrayList<Route>
      >(HttpMethod::class.java)

  fun bind(path: String, method: HttpMethod, handler: (Request) -> Response) {
    if (!map.contains(method)) {
      map[method] = ArrayList()
    }

    map[method]?.add(
      Route(path, method, handler)
    )
  }

  fun invoke(request: Request): Response {
    if (!map.containsKey(request.method)) {
      throw HttpException(StatusCode.NOT_FOUND, "Handler for route not found")
    }

    val route = map[request.method]!!.find { it.match(request) }
    if (route == null) {
      throw HttpException(StatusCode.NOT_FOUND, "Handler for route not found")
    }

    return route.invoke(request)
  }

  class Route(
    private val path: String,
    private val method: HttpMethod,
    private val handler: (Request) -> Response
  ) {
    fun match(request: Request): Boolean {
      return path == request.uri
    }

    fun invoke(request: Request): Response {
      return handler(request)
    }
  }
}