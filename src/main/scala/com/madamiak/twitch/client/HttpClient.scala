package com.madamiak.twitch.client

import java.lang.Math.max
import java.time.Instant
import java.time.temporal.ChronoUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{ Path, Query }
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{ Unmarshal, Unmarshaller }
import akka.stream.ActorMaterializer
import com.madamiak.twitch.TwitchConfiguration.config
import com.madamiak.twitch.client.authentication.Authentication
import com.madamiak.twitch.model.api.TwitchPayload
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }
import com.typesafe.scalalogging.Logger
import retry.Success

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

trait HttpClient {
  this: Authentication =>

  type TwitchPayloadUnmarshaller[T] = Unmarshaller[ResponseEntity, TwitchPayload[T]]

  private val logger = Logger[HttpClient]

  private[client] implicit val system: ActorSystem
  private[client] implicit val context: ExecutionContext
  private[client] implicit val materializer: ActorMaterializer

  require(config.hasPath("twitch.api.scheme"), "Twitch api schema not defined")
  require(config.hasPath("twitch.api.host"), "Twitch api host not defined")

  private val twitchUri: Uri = Uri()
    .withScheme(config.getString("twitch.api.scheme"))
    .withHost(config.getString("twitch.api.host"))

  private[client] def payloadRequest[T](path: String, query: Query): Future[HttpRequest] =
    authenticationHeader().map(
      HttpRequest()
        .withUri(
          twitchUri
            .withPath(Path(path))
            .withQuery(query)
        )
        .withHeaders(_)
    )

  def http[T](path: String)(query: Query)(implicit m: TwitchPayloadUnmarshaller[T]): Future[TwitchResponse[T]] =
    recoverWhenRateLimitExceeded {
      recoverWhenUnauthorized {
        payloadRequest(path, query)
          .flatMap(Http().singleRequest(_))
      }
    }.flatMap(extractPayload[T])

  private[client] def extractPayload[T](
      response: HttpResponse
  )(implicit m: TwitchPayloadUnmarshaller[T]): Future[TwitchResponse[T]] = response.status match {
    case StatusCodes.OK =>
      Unmarshal(response.entity)
        .to[TwitchPayload[T]]
        .map(data => TwitchResponse(RateLimit(response), data))
    case StatusCodes.Unauthorized => Future.failed(new TwitchAPIException(s"Twitch client '$clientId' unauthorized"))
    case code                     => Future.failed(new TwitchAPIException(s"Twitch server respond with code $code"))
  }

  private[client] def recoverWhenRateLimitExceeded(in: => Future[HttpResponse]): Future[HttpResponse] = {
    implicit val success: Success[HttpResponse] = Success(_.status == StatusCodes.OK)
    retry.When {
      case response @ HttpResponse(StatusCodes.TooManyRequests, _, _, _) =>
        val rateLimit = RateLimit(response)
        val until     = Instant.now.until(Instant.ofEpochSecond(rateLimit.reset), ChronoUnit.SECONDS)
        val pause     = max(0, until).seconds
        logger.info(s"Request rate limit exceeded. Waiting $until seconds to retry")
        retry.Pause(delay = pause)
    }(in)
  }

}
