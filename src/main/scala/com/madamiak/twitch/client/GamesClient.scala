package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.header.ClientIdHeader
import com.madamiak.twitch.model.{Game, TwitchData}
import com.typesafe.config.ConfigFactory._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Gets game information by game ID or name.
  *
  * @param system akka actor system
  * @param context execution context
  * @param materializer actor materializer
  */
class GamesClient(implicit val system: ActorSystem,
                  implicit val context: ExecutionContext,
                  implicit val materializer: ActorMaterializer) {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

//  def getGamesByName(names: Seq[String]): Future[TwitchData[Game]] =
//    Http()
//      .singleRequest(
//        HttpRequest()
//          .withUri(
//            twitchUri
//              .withPath(Path(gamesPath))
//              .withQuery(names.toQuery("id"))
//          )
//          .withHeaders(
//            ClientIdHeader(load().getString("twitch.client.id"))
//          )
//      )
//      .flatMap(extractData[Game])

  def getGamesById(ids: Seq[Long]): Future[TwitchData[Game]] =
    Http()
      .singleRequest(
        HttpRequest()
          .withUri(
            twitchUri
              .withPath(Path(gamesPath))
              .withQuery(ids.toQuery("id"))
          )
          .withHeaders(
            ClientIdHeader(load().getString("twitch.client.id"))
          )
      )
      .flatMap(extractData[Game])

  private val twitchUri: Uri = Uri()
    .withScheme(load().getString("twitch.api.scheme"))
    .withHost(load().getString("twitch.api.host"))

  def extractData[T](response: HttpResponse)(implicit m: Unmarshaller[ResponseEntity, TwitchData[T]]): Future[TwitchData[T]] = response.status match {
    case StatusCodes.OK => Unmarshal(response.entity).to[TwitchData[T]]
    case code           => Future.failed(new TwitchAPIException(s"Twitch server response was $code"))
  }
}
