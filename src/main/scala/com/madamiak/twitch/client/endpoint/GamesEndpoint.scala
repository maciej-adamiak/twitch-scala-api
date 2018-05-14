package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.game.TwitchGame

import scala.concurrent.{ ExecutionContext, Future }

class GamesEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

  /**
    * Gets game information by game name
    *
    * @param names Game names sequence
    * @return Twitch game data
    */
  def getByName(names: Seq[String]): Future[TwitchResponse[TwitchGame]] = ~> {

    require(names.nonEmpty, "Cannot query using empty names list")
    require(names.length <= 100, "Cannot query using more than 100 names")

    client.http(gamesPath) {
      query("name" -> names)
    }
  }

  /**
    * Gets game information by game ids
    *
    * @param ids Game ids
    * @return Twitch game data
    */
  def getById(ids: Seq[String]): Future[TwitchResponse[TwitchGame]] = ~> {
    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")

    client.http(gamesPath) {
      query("id" -> ids)
    }
  }

  /**
    * Gets games sorted by number of current viewers on Twitch, most popular first
    *
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch game data
    */
  def popular(before: Option[String] = None,
              after: Option[String] = None,
              first: Option[Int] = None): Future[TwitchResponse[TwitchGame]] = ~> {
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(topGamesPath) {
      query(
        "before" -> before,
        "after"  -> after,
        "first"  -> first
      )
    }
  }
}
