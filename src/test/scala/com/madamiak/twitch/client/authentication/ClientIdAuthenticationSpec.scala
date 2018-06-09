package com.madamiak.twitch.client.authentication

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import org.scalatest.{ AsyncWordSpec, Matchers }

import scala.concurrent.{ ExecutionContext, Future }

class ClientIdAuthenticationSpec extends AsyncWordSpec with Matchers {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  class TestAuthentication(
      implicit val system: ActorSystem,
      implicit val context: ExecutionContext,
      implicit val materializer: ActorMaterializer
  ) {
    this: Authentication =>
  }

  private val sut = new TestAuthentication() with ClientIdAuthentication

  "enables recovery" should {

    "return same value" in {
      val response = Future.successful(HttpResponse())

      sut.recoverWhenUnauthorized(response) shouldEqual response
    }

  }
}
