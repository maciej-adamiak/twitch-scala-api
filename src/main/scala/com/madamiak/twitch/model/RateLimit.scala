package com.madamiak.twitch.model

import akka.http.scaladsl.model.HttpResponse

case class RateLimit(limit: Int, remaining: Int, reset: Long)

object RateLimit {

  def apply(limit: Int, remaining: Int, reset: Long): RateLimit = new RateLimit(limit, remaining, reset)

  def apply(httpResponse: HttpResponse): RateLimit = {
    val limit = httpResponse.getHeader("Ratelimit-Limit").get.value.toInt
    val remaining = httpResponse.getHeader("Ratelimit-Remaining").get.value.toInt
    val reset = httpResponse.getHeader("Ratelimit-Reset").get.value.toLong
    new RateLimit(limit, remaining, reset)
  }

}