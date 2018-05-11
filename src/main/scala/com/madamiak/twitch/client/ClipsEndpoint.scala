package com.madamiak.twitch.client

import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.clip.TwitchClip

import scala.concurrent.{ ExecutionContext, Future }

class ClipsEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  private val clipsPath = "/helix/clips"

  /**
    * Gets information about top clips for a specified game
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and a pagination field containing information required to query for more streams.
    *
    * @param gameId ID of the game for which top clips are returned
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch clip data
    */
  def getByGameId(gameId: String,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchClip]] = ~> {

    require(gameId.nonEmpty, "Cannot query using empty game id")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      Map(
        "game_id" -> Some(gameId),
        "before"  -> before,
        "after"   -> after,
        "first"   -> first
      ).query
    }
  }

  /**
    * Gets information about specified clips
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and a pagination field containing information required to query for more streams.
    *
    * @param ids ID of the clip being queried
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch clip data
    */
  def getById(ids: Seq[String],
              before: Option[String] = None,
              after: Option[String] = None,
              first: Option[Int] = None): Future[TwitchResponse[TwitchClip]] = ~> {

    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      val queryParameters = Map("id" -> ids).query
      val paginationParameters = Map(
        "before" -> before,
        "after"  -> after,
        "first"  -> first
      ).query
      queryParameters merge paginationParameters
    }
  }

  /**
    * Gets information about top clips for a specified broadcaster
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and a pagination field containing information required to query for more streams.
    *
    * @param broadcasterId ID of the broadcaster for whom top clips are returned
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch clip data
    */
  def getByBroadcasterId(broadcasterId: String,
                         before: Option[String] = None,
                         after: Option[String] = None,
                         first: Option[Int] = None): Future[TwitchResponse[TwitchClip]] = ~> {

    require(broadcasterId.nonEmpty, "Cannot query using empty broadcaster id")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      Map(
        "broadcaster_id" -> Some(broadcasterId),
        "before"         -> before,
        "after"          -> after,
        "first"          -> first
      ).query
    }
  }

}
