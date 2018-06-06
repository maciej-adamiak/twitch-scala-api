package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{ Path, Query }
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{ Unmarshal, Unmarshaller }
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.authentication.Authentication
import com.madamiak.twitch.model.api.TwitchPayload
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.{ ExecutionContext, Future }

trait HttpClient {
  this: Authentication =>

  private[client] implicit val system: ActorSystem
  private[client] implicit val context: ExecutionContext
  private[client] implicit val materializer: ActorMaterializer

  require(config.hasPath("twitch.api.scheme"), "Twitch api schema not defined")
  require(config.hasPath("twitch.api.host"), "Twitch api host not defined")

  private val twitchUri: Uri = Uri()
    .withScheme(config.getString("twitch.api.scheme"))
    .withHost(config.getString("twitch.api.host"))

  private[client] def request[T](path: String, query: Query) =
    HttpRequest()
      .withUri(
        twitchUri
          .withPath(Path(path))
          .withQuery(query)
      )
      .withHeaders(authenticationHeader())

  def http[T](
      path: String
  )(query: Query)(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] =
    Http()
      .singleRequest(request(path, query))
      .flatMap(response[T])

  private[client] def response[T](
      response: HttpResponse
  )(implicit m: Unmarshaller[ResponseEntity, TwitchPayload[T]]): Future[TwitchResponse[T]] = response.status match {
    case StatusCodes.OK =>
      Unmarshal(response.entity)
        .to[TwitchPayload[T]]
        .map(data => TwitchResponse(RateLimit(response), data))
    case StatusCodes.Unauthorized =>
      Future.failed(
        new TwitchAPIException(
          s"Twitch client with id '$clientId' has not been authenticated"
        )
      )
    case code => Future.failed(new TwitchAPIException(s"Twitch server respond with code $code"))
  }

}
