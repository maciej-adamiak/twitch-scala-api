package com.madamiak.twitch.model

import akka.http.scaladsl.model.HttpResponse

/**
  * Each client ID is granted a total of 30 queries per minute (if a Bearer token is not provided) or 120 queries per minute
  * (if a Bearer token is provided), across all new Twitch API queries. If this limit is exceeded, an error is returned
  *
  * @param limit The number of requests you can use for the rate-limit window
  * @param remaining  The number of requests you have left to use for the rate-limit window
  * @param reset A Unix epoch timestamp of when your rate-limit window will reset
  */
case class RateLimit(limit: Int, remaining: Int, reset: Long)

object RateLimit {

  def apply(limit: Int, remaining: Int, reset: Long): RateLimit = new RateLimit(limit, remaining, reset)

  def apply(httpResponse: HttpResponse): RateLimit = {
    val limit     = httpResponse.getHeader("Ratelimit-Limit").get.value.toInt
    val remaining = httpResponse.getHeader("Ratelimit-Remaining").get.value.toInt
    val reset     = httpResponse.getHeader("Ratelimit-Reset").get.value.toLong
    new RateLimit(limit, remaining, reset)
  }

}
