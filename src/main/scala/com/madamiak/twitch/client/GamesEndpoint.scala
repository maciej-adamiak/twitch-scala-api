package com.madamiak.twitch.client

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.Game

import scala.concurrent.{ExecutionContext, Future}

class GamesEndpoint(implicit val context: ExecutionContext, implicit val client: TwitchClient) extends Endpoint {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

  /**
    * Gets game information by game name
    *
    * @param names Game name
    * @return Twitch game data
    */
  def getGamesByName(names: Seq[String]): Future[TwitchResponse[Game]] = {
    require(names.length <= 100, "Cannot query using more than 100 names")
    client.http(gamesPath)(names.toQuery("name"))
  }

  /**
    * Gets game information by game id
    *
    * @param ids game ids
    * @return Twitch game data
    */
  def getGamesById(ids: Seq[Long]): Future[TwitchResponse[Game]] =
    Future {
      require(ids.nonEmpty, "Cannot query using empty ids")
      require(ids.length <= 100, "Cannot query using more than 100 ids")
    }.flatMap(_ => client.http(gamesPath)(ids.toQuery("id")))

  /**
    * Gets games sorted by number of current viewers on Twitch, most popular first
    *
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return
    */
  def getTopGames(before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[Game]] =
    client.http(topGamesPath) {

      require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

      Query {
        Map(
          "before" -> before,
          "after"  -> after,
          "first"  -> first
        ).filter(_._2.isDefined).mapValues(_.get.toString)
      }
    }

}
