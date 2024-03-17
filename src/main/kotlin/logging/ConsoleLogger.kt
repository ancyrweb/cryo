package fr.cryo.logging

import java.time.LocalDateTime

class ConsoleLogger : Logger {
  private val ANSI_RESET: String = "\u001B[0m"
  private val ANSI_BLACK: String = "\u001B[30m"
  private val ANSI_RED: String = "\u001B[31m"
  private val ANSI_GREEN: String = "\u001B[32m"
  private val ANSI_YELLOW: String = "\u001B[33m"
  private val ANSI_BLUE: String = "\u001B[34m"
  private val ANSI_PURPLE: String = "\u001B[35m"
  private val ANSI_CYAN: String = "\u001B[36m"
  private val ANSI_WHITE: String = "\u001B[37m"

  override fun info(message: String) {
    print(message)
  }

  override fun success(message: String) {
    printWithColor(ANSI_GREEN, message)
  }

  override fun error(message: String) {
    printWithColor(ANSI_RED, message)
  }

  private fun print(message: String) {
    val date = LocalDateTime.now()
    println("[$date] $message")
  }

  private fun printWithColor(color: String, message: String) {
    val date = LocalDateTime.now()
    println("$color[$date] $message$ANSI_RESET")
  }
}