package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.ExecutionContext

class GamesEndpointSpec extends WordSpec with Matchers with MockFactory {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val executor: ExecutionContext      = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val stub = stub[GamesEndpoint]

  "games endpoint" when {

    "performing a request to acquire games by id" should {

      "return single result" in {
        ???
      }

    }

  }

}
