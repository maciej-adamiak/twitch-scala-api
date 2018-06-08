package com.madamiak.twitch.client.authentication

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.madamiak.twitch.TwitchConfiguration.config
import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchAPIException
import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.authentication.AuthenticationData
import com.typesafe.scalalogging.Logger
import retry.Success

import scala.collection.JavaConverters._
import scala.concurrent.{ ExecutionContext, Future }

/**
  * Provides advanced OAuth authentication capabilities. Application access tokens utilize client credentials.
  * They enable performing secure API requests that are not on behalf of a specific user but an appliaction.
  *
  * @see https://dev.twitch.tv/docs/authentication/#types-of-tokens
  */
trait ApplicationAccessTokenAuthentication extends Authentication {

  implicit val system: ActorSystem
  implicit val context: ExecutionContext
  implicit val materializer: ActorMaterializer

  private val logger = Logger[ApplicationAccessTokenAuthentication]

  require(config.hasPath("twitch.id.scheme"), "Twitch id api scheme not defined")
  require(config.hasPath("twitch.id.host"), "Twitch id api host not defined")
  require(config.hasPath("twitch.client.secret"), "Twitch api client secret not defined")
  require(config.hasPath("twitch.client.scopes"), "Twitch api client scopes not defined")

  private val clientSecret = config.getString("twitch.client.secret")
  private val clientScopes = config.getStringList("twitch.client.scopes").asScala.mkString(" ")

  private val twitchIdUri: Uri = Uri()
    .withScheme(config.getString("twitch.id.scheme"))
    .withHost(config.getString("twitch.id.host"))
    .withPath(Path(config.getString("twitch.id.path")))

  private val tokenStorage = new TokenStorage()

  override def recoverWhenUnauthorized(in: => Future[HttpResponse]): Future[HttpResponse] = {
    implicit val success: Success[HttpResponse] = Success(_.status == StatusCodes.OK)
    retry.When {
      case HttpResponse(StatusCodes.Unauthorized, _, _, _) =>
        logger.info("Request has not been authorized. Refreshing access token and performing a retry")
        tokenStorage.evict()
        retry.Directly(max = 0)
    }(in)
  }

  override def authenticationHeader(): Future[HttpHeader] =
    tokenStorage
      .store {
        Http()
          .singleRequest(tokenRequest)
          .flatMap(extractToken)

      }
      .map(token => RawHeader("Authorization", s"Bearer $token"))

  private def extractToken(response: HttpResponse) = response.status match {
    case StatusCodes.OK =>
      Unmarshal(response.entity)
        .to[AuthenticationData]
        .map(_.accessToken)
    case _ =>
      Future.failed(
        new TwitchAPIException(
          s"Twitch client with id '$clientId' is not able to acquire OAuth token"
        )
      )
  }

  private def tokenRequest =
    HttpRequest()
      .withMethod(HttpMethods.POST)
      .withUri(
        twitchIdUri.withQuery(
          query(
            "client_id"     -> clientId,
            "client_secret" -> clientSecret,
            "grant_type"    -> "client_credentials",
            "scope"         -> clientScopes
          )
        )
      )
}
