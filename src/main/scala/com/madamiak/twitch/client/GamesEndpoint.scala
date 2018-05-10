package com.madamiak.twitch.client

import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.game.TwitchGame

import scala.concurrent.{ ExecutionContext, Future }

class GamesEndpoint(implicit private val context: ExecutionContext, implicit private[client] val client: TwitchClient)
    extends Endpoint {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

  /**
    * Gets game information by game name
    *
    * @param names Game name
    * @return Twitch game data
    */
  def getGamesByName(names: Seq[String]): Future[TwitchResponse[TwitchGame]] =
    Future {
      require(names.nonEmpty, "Cannot query using empty ids list")
      require(names.length <= 100, "Cannot query using more than 100 names")
    }.flatMap(
      _ =>
        client.http(gamesPath) {
          Map("name" -> names).query
      }
    )

  /**
    * Gets game information by game id
    *
    * @param ids game ids
    * @return Twitch game data
    */
  def getGamesById(ids: Seq[String]): Future[TwitchResponse[TwitchGame]] =
    Future {
      require(ids.nonEmpty, "Cannot query using empty ids list")
      require(ids.length <= 100, "Cannot query using more than 100 ids")
    }.flatMap(
      _ =>
        client.http(gamesPath) {
          Map("id" -> ids).query
      }
    )

  /**
    * Gets games sorted by number of current viewers on Twitch, most popular first
    *
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch game data
    */
  def getTopGames(before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchGame]] =
    Future {
      require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")
    }.flatMap { _ =>
      client.http(topGamesPath) {
        Map(
          "before" -> before,
          "after"  -> after,
          "first"  -> first
        ).query
      }
    }
}
