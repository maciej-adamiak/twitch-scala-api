package com.madamiak.twitch.client.authentication

import org.scalatest.{ Matchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class TokenStorageSpec extends WordSpec with Matchers {

  def token: Future[String] = Future.successful("token")

  "token storage" should {

    "store token" in {
      val sut = new TokenStorage()
      Await.result(sut.store(token), Duration.Inf)
      sut.stats().hitCount() shouldBe 0
      sut.stats().missCount() shouldBe 1

      Await.result(sut.store(token), Duration.Inf)
      sut.stats().hitCount() shouldBe 1
      sut.stats().missCount() shouldBe 1
    }

    "evict token" in {
      val sut = new TokenStorage()
      Await.result(sut.store(token), Duration.Inf)
      sut.stats().hitCount() shouldBe 0
      sut.stats().missCount() shouldBe 1

      Await.result(sut.evict(), Duration.Inf)

      Await.result(sut.store(token), Duration.Inf)
      sut.stats().hitCount() shouldBe 0
      sut.stats().missCount() shouldBe 2
    }
  }
}
