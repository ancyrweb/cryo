package fr.cryo.http.routing

import fr.cryo.http.HttpMethod
import fr.cryo.http.Request
import fr.cryo.http.Response

interface Router {
  fun bind(
    path: String,
    method: HttpMethod,
    handler: (request: Request, response: Response) -> Unit
  )

  fun invoke(request: Request, response: Response): Unit
}