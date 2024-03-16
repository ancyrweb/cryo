package fr.cryo.http

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

  class RequestTask(val socket: Socket, val router: Router) : Runnable {
    override fun run() {
      val inputStream = socket.getInputStream()
      var response: Response?


      if (inputStream.available() == 0) {
        response = Response(
          body = "",
          headers = Headers().apply {
            add("Content-Type", "text/plain")
            add("X-WebServer", "Cryo")
          },
          status = StatusCode.BAD_REQUEST
        )
      } else {
        val request = Request(socket.getInputStream())
        try {
          response = router.invoke(request)
        } catch (e: HttpException) {
          e.printStackTrace()
          response = Response(
            body = e.message,
            headers = Headers().apply {
              add("Content-Type", "text/plain")
              add("X-WebServer", "Cryo")
            },
            status = e.code
          )
        } catch (e: Exception) {
          e.printStackTrace()
          response = Response(
            body = e.message,
            headers = Headers().apply {
              add("Content-Type", "text/plain")
              add("X-WebServer", "Cryo")
            },
            status = StatusCode.INTERNAL_SERVER_ERROR
          )
        }
      }

      if (response == null) {
        response = Response(
          body = "An unknown error occured",
          headers = Headers().apply {
            add("Content-Type", "text/plain")
            add("X-WebServer", "Cryo")
          },
          status = StatusCode.INTERNAL_SERVER_ERROR
        )
      }

      socket
        .getOutputStream()
        .write(response.toByteArray())

      socket.close()
    }
  }
}