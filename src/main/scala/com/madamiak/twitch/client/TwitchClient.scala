package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{Path, Query}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.header.ClientIdHeader
import com.madamiak.twitch.model._
import com.madamiak.twitch.model.api.TwitchPayload
import com.typesafe.config.ConfigFactory.load

import scala.concurrent.{ExecutionContext, Future}

class TwitchClient(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer,
) {

  require(load().hasPath("twitch.api.scheme"), "Twitch api schema not defined")
  require(load().hasPath("twitch.api.host"), "Twitch api host not defined")
  require(load().hasPath("twitch.client.id"), "Twitch api client id not defined")

  private val twitchUri: Uri = Uri()
    .withScheme(load().getString("twitch.api.scheme"))
    .withHost(load().getString("twitch.api.host"))

  def http[T](
      path: String
  )(query: Query)(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] =
    Http()
      .singleRequest(
        HttpRequest()
          .withUri(
            twitchUri
              .withPath(Path(path))
              .withQuery(query)
          )
          .withHeaders(ClientIdHeader(load().getString("twitch.client.id")))
      )
      .flatMap(extractData[T])

  private def extractData[T](
      response: HttpResponse
  )(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] = response.status match {
    case StatusCodes.OK =>
      Unmarshal(response.entity)
        .to[TwitchPayload[T]]
        .map(data => TwitchResponse(RateLimit(response), data))
    case code => Future.failed(new TwitchAPIException(s"Twitch server response was $code"))
  }
}
