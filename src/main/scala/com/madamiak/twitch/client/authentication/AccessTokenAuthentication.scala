package com.madamiak.twitch.client.authentication

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchAPIException
import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.authentication.AuthenticationData

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }

trait AccessTokenAuthentication extends Authentication {

  implicit val system: ActorSystem
  implicit val context: ExecutionContext
  implicit val materializer: ActorMaterializer

  require(config.hasPath("twitch.client.secret"), "Twitch api client secret not defined")

  private val twitchIdUri: Uri = Uri()
    .withScheme(config.getString("twitch.id.scheme"))
    .withHost(config.getString("twitch.id.host"))
    .withPath(Path(config.getString("twitch.id.path")))

  private val clientSecret = config.getString("twitch.client.secret")

  // TODO scopes
  // TODO reuse token
  override def authenticationHeader(): HttpHeader =
    Await.result(
      Http()
        .singleRequest(
          HttpRequest()
            .withMethod(HttpMethods.POST)
            .withUri(
              twitchIdUri.withQuery(
                query(
                  "client_id"     -> clientId,
                  "client_secret" -> clientSecret,
                  "grant_type"    -> "client_credentials"
                )
              )
            )
        )
        .flatMap(
          response =>
            response.status match {
              case StatusCodes.OK =>
                Unmarshal(response.entity)
                  .to[AuthenticationData]
                  .map(_.accessToken)
                  .map(token => RawHeader("Authorization", s"Bearer $token"))
              case _ =>
                Future.failed(
                  new TwitchAPIException(
                    s"Twitch client with id '$clientId' is not able to acquire OAuth token"
                  )
                )
          }
        ),
//      TODO do not use fixed duration
      Duration.apply(5, TimeUnit.SECONDS)
    )

}
