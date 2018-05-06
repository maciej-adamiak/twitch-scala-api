package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{ Path, Query }
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.header.ClientIdHeader
import com.madamiak.twitch.model.{ Game, TwitchData }
import com.typesafe.config.ConfigFactory._

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Gets game information by game ID or name.
  *
  * @param system akka actor system
  * @param context execution context
  * @param materializer actor materializer
  */
class GamesEndpoint(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer
) {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

  val client = new TwitchClient()

  def getGamesByName(names: Seq[String]): Future[TwitchData[Game]] =
    client.http(gamesPath)(names.toQuery("id"))

  def getGamesById(ids: Seq[Long]): Future[TwitchData[Game]] =
    client.http(gamesPath)(ids.toQuery("id"))

  def getTopGames(before: Option[String] = None,
                  after: Option[String] = None,
                  first: Int = 20): Future[TwitchData[Game]] =
    client.http(topGamesPath) {
      val parameters = Map[String, Option[String]](
        "before" -> before,
        "after"  -> after,
        "first"  -> Some(first.toString)
      ).filter(_._2.isDefined).mapValues(_.get)

      Query(parameters)
    }

}
