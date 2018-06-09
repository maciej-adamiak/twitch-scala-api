package com.madamiak.twitch.client.authentication

import akka.actor.ActorSystem
import org.scalatest.{ Matchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class TokenStorageSpec extends WordSpec with Matchers {

  implicit val system: ActorSystem = ActorSystem("test-actor-system")

  def success: Future[String] = Future.successful("token")
  def fail: Future[String]    = Future.failed(new IllegalArgumentException)

  "token storage" should {

    "store token" in {
      val sut = new TokenStorage()
      sut.size() shouldBe 0

      Await.result(sut.store(success), Duration.Inf)
      sut.size() shouldBe 1

      Await.result(sut.store(success), Duration.Inf)
      sut.size() shouldBe 1
    }

    "store failed" in {
      val sut = new TokenStorage()
      sut.size() shouldBe 0
      intercept[IllegalArgumentException](
        Await.result(sut.store(fail), Duration.Inf)
      )
      sut.size() shouldBe 1

      intercept[IllegalArgumentException](
        Await.result(sut.store(success), Duration.Inf)
      )
      sut.size() shouldBe 1
    }

    "evict token" in {
      val sut = new TokenStorage()
      sut.size() shouldBe 0
      Await.result(sut.store(success), Duration.Inf)
      sut.size() shouldBe 1

      sut.evict()

      sut.size() shouldBe 0
      Await.result(sut.store(success), Duration.Inf)
      sut.size() shouldBe 1
    }
  }
}
