package fr.cryo.http

import fr.cryo.http.routing.Router
import fr.cryo.logging.ConsoleLogger
import fr.cryo.logging.Logger
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HttpServer(
  port: Int,
  private val logger: Logger = ConsoleLogger()
) {
  private var isRunning = true
  private val serverSocket = ServerSocket(port)
  private lateinit var threadPool: ExecutorService
  private var router: Router? = null


  /**
   * Runs the Cryo server
   */
  fun run() {
    if (router == null) {
      throw RuntimeException("No router defined")
    }

    isRunning = true
    threadPool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors()
    )

    logger.info("Cryo Server is starting")

    while (isRunning) {
      try {
        val socket = serverSocket.accept()
        threadPool.submit(RequestTask(socket))
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun use(router: Router) {
    this.router = router
  }


  /**
   * Shuts the server down
   * Respond to the last queued requests and then clean the thread pool.
   */
  fun shutdown() {
    logger.info("Shutting down")

    isRunning = false
    serverSocket.close()
    threadPool.shutdown()
  }

  /**
   * Reoresent a request flow
   */
  inner class RequestTask(
    private val socket: Socket,
  ) : Runnable {
    override fun run() {
      val inputStream = socket.getInputStream()

      val response = Response()
      response.headers.set("X-WebServer", "Cryo")

      if (inputStream.available() == 0) {
        response.setStatusCode(StatusCode.BAD_REQUEST)
        response.respondText("Bad request")
        end(response)
      }

      val request = Request.Builder(socket.getInputStream()).build()
      logger.info(request.toStringSummary())

      try {
        router!!.invoke(request, response)
      } catch (e: HttpException) {
        logger.error(e.stackTraceToString())
        response.setStatusCode(e.code)
        response.respondText(e.message ?: "An unknown error occured")
      } catch (e: Exception) {
        logger.error(e.stackTraceToString())
        response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
        response.respondText(e.message ?: "An unknown error occured")
      }

      if (response.isSuccess()) {
        logger.success(response.toStringSummary())
      } else {
        logger.error(response.toStringSummary())
      }

      end(response)
    }

    private fun end(response: Response) {
      socket
        .getOutputStream()
        .write(response.toByteArray())

      socket.close()
    }
  }
}