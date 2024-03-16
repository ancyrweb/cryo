package fr.cryo.http

import fr.cryo.http.routing.Router
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HttpServer(
  port: Int
) {
  private var isRunning = true
  private val serverSocket = ServerSocket(port)
  private lateinit var threadPool: ExecutorService
  private var router: Router? = null

  fun run() {
    if (router == null) {
      throw RuntimeException("No router defined")
    }

    isRunning = true
    threadPool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors()
    )

    while (isRunning) {
      try {
        val socket = serverSocket.accept()
        threadPool.submit(RequestTask(socket, router!!))
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun use(router: Router) {
    this.router = router
  }

  fun shutdown() {
    isRunning = false
    serverSocket.close()
    threadPool.shutdown()
  }

  class RequestTask(
    private val socket: Socket,
    private val router: Router
  ) : Runnable {
    override fun run() {
      val inputStream = socket.getInputStream()
      val response = Response()
      response.headers.set("X-WebServer", "Cryo")

      if (inputStream.available() == 0) {
        response.setStatusCode(StatusCode.BAD_REQUEST)
        response.setPlainTextBody("Bad request")
      } else {
        val request = Request(socket.getInputStream())
        try {
          router.invoke(request, response)
        } catch (e: HttpException) {
          e.printStackTrace()
          response.setStatusCode(e.code)
          response.setPlainTextBody(e.message ?: "An unknown error occured")
        } catch (e: Exception) {
          e.printStackTrace()
          response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
          response.setPlainTextBody(e.message ?: "An unknown error occured")
        }
      }

      socket
        .getOutputStream()
        .write(response.toByteArray())

      socket.close()
    }
  }
}