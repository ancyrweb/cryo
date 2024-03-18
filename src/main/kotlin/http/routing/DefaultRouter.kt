package fr.cryo.http.routing

import fr.cryo.http.*
import java.util.*


class DefaultRouter : Router {
  private val map = EnumMap<
      HttpMethod,
      ArrayList<Route>
      >(HttpMethod::class.java)

  override fun bind(
    path: String,
    method: HttpMethod,
    handler: (Request, Response) -> Unit
  ) {
    if (!map.contains(method)) {
      map[method] = ArrayList()
    }

    map[method]?.add(
      Route(path, handler)
    )
  }

  override fun invoke(request: Request, response: Response): Unit {
    if (!map.containsKey(request.method)) {
      throw HttpException(
        StatusCode.NOT_FOUND,
        "Handler for route " + request.toStringSummary() + " not found"
      )
    }

    val route = map[request.method]!!.find { it.match(request) }
    if (route == null) {
      throw HttpException(
        StatusCode.NOT_FOUND,
        "Handler for route " + request.toStringSummary() + " not found"
      )
    }

    route.invoke(request, response)
  }

  class Route(
    private val path: String,
    private val handler: (Request, Response) -> Unit
  ) {
    fun match(request: Request): Boolean {
      return path == request.url.path
    }

    fun invoke(request: Request, response: Response): Unit {
      return handler(request, response)
    }
  }
}