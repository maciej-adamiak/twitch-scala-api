package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.model.api.JsonSupport
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{ AsyncWordSpec, Matchers }

import scala.concurrent.ExecutionContext

trait EndpointWordSpec extends AsyncWordSpec with Matchers with AsyncMockFactory with JsonSupport {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val executor: ExecutionContext      = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val twitchClient: TwitchClient      = mock[TwitchClient]

}
