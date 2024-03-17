package fr.cryo.logging

interface Logger {
  fun info(message: String): Unit
  fun error(message: String): Unit
  fun success(message: String): Unit
}