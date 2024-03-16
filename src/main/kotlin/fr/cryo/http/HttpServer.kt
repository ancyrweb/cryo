package fr.cryo.http

import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HttpServer(port: Int) {
  private var isRunning = true
  private val serverSocket = ServerSocket(port)
  private lateinit var threadPool: ExecutorService

  fun run() {
    isRunning = true
    threadPool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors()
    )

    while (isRunning) {
      try {
        val socket = serverSocket.accept()
        threadPool.submit(RequestTask(socket))
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun shutdown() {
    isRunning = false
    serverSocket.close()
    threadPool.shutdown()
  }

  class RequestTask(val socket: Socket) : Runnable {
    override fun run() {
      val inputStream = socket.getInputStream()
      val response: Response?


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

        response = Response(
          body = "Hello, World!",
          headers = Headers().apply {
            add("Content-Type", "text/plain")
            add("X-WebServer", "Cryo")
          },
          status = StatusCode.CREATED
        )
      }

      socket
        .getOutputStream()
        .write(response.toByteArray())

      socket.close()
    }
  }
}