package com.madamiak.twitch.client.authentication

import org.scalatest.{ Matchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class TokenStorageSpec extends WordSpec with Matchers {

  def success: Future[String] = Future.successful("token")
  def fail: Future[String]    = Future.failed(new IllegalArgumentException)

  "token storage" should {

    "store token" in {
      val sut = new TokenStorage()
      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 0
      sut.missCount() shouldBe 1

      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 1
      sut.missCount() shouldBe 1
    }

    "store only successful" in {

      val sut = new TokenStorage()
      try {
        Await.result(sut.store(fail), Duration.Inf)
      } catch {
        case e: IllegalArgumentException => println("")
      }
      sut.hitCount() shouldBe 0
      sut.missCount() shouldBe 1

      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 0
      sut.missCount() shouldBe 2

      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 1
      sut.missCount() shouldBe 2
    }

    "evict token" in {
      val sut = new TokenStorage()
      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 0
      sut.missCount() shouldBe 1

      Await.result(sut.evict(), Duration.Inf)

      Await.result(sut.store(success), Duration.Inf)
      sut.hitCount() shouldBe 0
      sut.missCount() shouldBe 2
    }
  }
}
