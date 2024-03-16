package fr.cryo.http

class HttpException(val code: StatusCode, message: String) :
  RuntimeException(message)