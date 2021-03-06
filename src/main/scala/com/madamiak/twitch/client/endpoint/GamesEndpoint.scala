package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.HttpClient
import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.game.TwitchGame

import scala.concurrent.{ ExecutionContext, Future }

class GamesEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: HttpClient
) extends Endpoint {

  private val gamesPath    = "/helix/games"
  private val topGamesPath = "/helix/games/top"

  /**
    * Acquire game information using a single game name
    *
    * @param name Game name
    * @return Twitch game data
    */
  def byName(name: String): Future[TwitchResponse[TwitchGame]] = byName(Seq(name))

  /**
    * Acquire game information using multiple game names
    *
    * @param names Game names
    * @return Twitch game data
    */
  def byName(names: Seq[String]): Future[TwitchResponse[TwitchGame]] = ~> {
    require(names.nonEmpty, "Cannot query using empty names list")
    require(names.length <= 100, "Cannot query using more than 100 names")

    client.http(gamesPath) {
      query("name" -> names)
    }
  }

  /**
    * Acquires game information using a single game id
    *
    * @param id Game ids
    * @return Twitch game data
    */
  def byId(id: String): Future[TwitchResponse[TwitchGame]] = byId(Seq(id))

  /**
    * Acquire game information using multiple game ids
    *
    * @param ids Game ids
    * @return Twitch game data
    */
  def byId(ids: Seq[String]): Future[TwitchResponse[TwitchGame]] = ~> {
    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")

    client.http(gamesPath) {
      query("id" -> ids)
    }
  }

  /**
    * Acquire games sorted by number of current viewers on Twitch, most popular first
    *
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return, if none defaults to 20
    * @return Twitch game data
    */
  def popular(
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchGame]] = ~> {
    require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(topGamesPath) {
      query(
        "before" -> before,
        "after"  -> after,
        "first"  -> size
      )
    }
  }
}
