package com.madamiak.twitch.model

import akka.http.scaladsl.model.HttpResponse

/**
  * Each client ID is granted a total of 30 queries per minute (if a Bearer token is not provided) or 120 queries per minute
  * (if a Bearer token is provided), across all new Twitch API queries. If this limit is exceeded, an error is returned
  *
  * @param limit     The number of requests you can use for the rate-limit window
  * @param remaining The number of requests you have left to use for the rate-limit window
  * @param reset     A Unix epoch timestamp of when your rate-limit window will reset
  */
case class RateLimit(limit: Int,
                     remaining: Int,
                     reset: Long,
                     streamMetadataLimit: Option[Int] = None,
                     streamMetadataRemaining: Option[Int] = None)

object RateLimit {

  def apply(limit: Int,
            remaining: Int,
            reset: Long,
            streamMetadataLimit: Option[Int] = None,
            streamMetadataRemaining: Option[Int] = None): RateLimit = {

    require(limit > 0, "rate limit should be greater than 0")
    require(remaining >= 0, "remaining limit should be non-negative")

    new RateLimit(limit, remaining, reset, streamMetadataLimit, streamMetadataRemaining)
  }

  def apply(httpResponse: HttpResponse): RateLimit = {
    val headers = httpResponse.headers.map(x => (x.lowercaseName(), x.value)).toMap

    val limit                   = headers("ratelimit-limit").toInt
    val remaining               = headers("ratelimit-remaining").toInt
    val reset                   = headers("ratelimit-reset").toLong
    val streamMetadataLimit     = headers.get("ratelimit-helixstreamsmetadata-limit").map(_.toInt)
    val streamMetadataRemaining = headers.get("ratelimit-helixstreamsmetadata-remaining").map(_.toInt)

    apply(limit, remaining, reset, streamMetadataLimit, streamMetadataRemaining)
  }

}
