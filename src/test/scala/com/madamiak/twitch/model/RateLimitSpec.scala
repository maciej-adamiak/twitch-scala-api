package com.madamiak.twitch.model

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers.RawHeader
import org.scalatest.{ Matchers, WordSpec }

class RateLimitSpec extends WordSpec with Matchers {

  "rate limit" when {

    "creating from HTTP response" should {

      "be successful when all rate limit data is available" in {
        val httpResponse: HttpResponse = HttpResponse()
          .addHeader(RawHeader("additional-header", "999"))
          .addHeader(RawHeader("", ""))
          .addHeader(RawHeader("ratelimit-limit", "1"))
          .addHeader(RawHeader("ratelimit-remaining", "2"))
          .addHeader(RawHeader("ratelimit-reset", "3"))

        RateLimit(httpResponse) shouldEqual RateLimit(1, 2, 3)
      }

      "be successful when all rate limit extended data is available" in {
        val httpResponse: HttpResponse = HttpResponse()
          .addHeader(RawHeader("additional-header", "999"))
          .addHeader(RawHeader("", ""))
          .addHeader(RawHeader("ratelimit-limit", "1"))
          .addHeader(RawHeader("ratelimit-remaining", "2"))
          .addHeader(RawHeader("ratelimit-reset", "3"))
          .addHeader(RawHeader("ratelimit-helixstreamsmetadata-limit", "4"))
          .addHeader(RawHeader("ratelimit-helixstreamsmetadata-remaining", "5"))

        RateLimit(httpResponse) shouldEqual RateLimit(1, 2, 3, Some(4), Some(5))
      }

      "should fail when rate limit data is not valid" in {
        val httpResponse: HttpResponse = HttpResponse()
          .addHeader(RawHeader("ratelimit-limit", "-100"))
          .addHeader(RawHeader("ratelimit-remaining", "2"))
          .addHeader(RawHeader("ratelimit-reset", "3"))

        intercept[IllegalArgumentException](RateLimit(httpResponse))
      }
    }
  }
}
