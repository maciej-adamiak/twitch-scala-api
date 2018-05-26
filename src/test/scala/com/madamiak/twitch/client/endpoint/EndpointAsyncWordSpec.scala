package com.madamiak.twitch.client.endpoint

import java.text.SimpleDateFormat

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.TwitchPayload
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{ AsyncWordSpec, Matchers }

import scala.concurrent.{ ExecutionContext, Future }

trait EndpointAsyncWordSpec extends AsyncWordSpec with Matchers with AsyncMockFactory {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val executor: ExecutionContext      = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val twitchClient: TwitchClient      = mock[TwitchClient]

  val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def twitchClientMock[T](path: String, query: Query, entity: T): TwitchClient = {
    val rateLimit = RateLimit(1, 2, 2)
    val payload   = TwitchPayload(Seq(entity), None)
    val result    = Future.successful(new TwitchResponse[T](rateLimit, payload))

    val twitchClient: TwitchClient = mock[TwitchClient]

    (twitchClient
      .http[T](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[T]])) expects (path, query, *) returns result

    twitchClient
  }

}
