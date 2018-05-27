package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{ Path, Query }
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{ Unmarshal, Unmarshaller }
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.header.ClientIdHeader
import com.madamiak.twitch.model._
import com.madamiak.twitch.model.api.TwitchPayload
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.{ ExecutionContext, Future }

class TwitchClient(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer,
) {

  private val config: Config = ConfigFactory.load()

  require(config.hasPath("twitch.api.scheme"), "Twitch api schema not defined")
  require(config.hasPath("twitch.api.host"), "Twitch api host not defined")
  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  private val twitchUri: Uri = Uri()
    .withScheme(config.getString("twitch.api.scheme"))
    .withHost(config.getString("twitch.api.host"))

  def http[T](
      path: String
  )(query: Query)(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] =
    Http()
      .singleRequest(request(path, query))
      .flatMap(extractData[T])

  private[client] def request[T](path: String, query: Query) =
    HttpRequest()
      .withUri(
        twitchUri
          .withPath(Path(path))
          .withQuery(query)
      )
      .withHeaders(ClientIdHeader(config.getString("twitch.client.id")))

  private[client] def extractData[T](
      response: HttpResponse
  )(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] = response.status match {
    case StatusCodes.OK =>
      Unmarshal(response.entity)
        .to[TwitchPayload[T]]
        .map(data => TwitchResponse(RateLimit(response), data))
    case code => Future.failed(new TwitchAPIException(s"Twitch server response was $code"))
  }
}
