package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.clip.TwitchClip

import scala.concurrent.{ ExecutionContext, Future }

class ClipsEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  private val clipsPath = "/helix/clips"

  /**
    * Acquire information regarding top clips for a specified game id
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and
    * a pagination field containing information required to query for more streams.
    *
    * @param gameId ID of the game for which top clips are returned
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch clip data
    */
  def byGameId(
      gameId: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchClip]] = ~> {
    require(gameId.nonEmpty, "Cannot query using empty game id")
    require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      query(
        "game_id" -> gameId,
        "before"  -> before,
        "after"   -> after,
        "first"   -> size
      )
    }
  }

  /**
    * Acquires information about specified clip
    *
    * @param id ID of the clip being queried
    * @return Twitch clip data
    */
  def byId(id: String): Future[TwitchResponse[TwitchClip]] = byId(Seq(id), None, None, Some(1))

  /**
    * Acquires information about specified clips
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and
    * a pagination field containing information required to query for more clips.
    *
    * @param ids ID of the clip being queried
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch clip data
    */
  def byId(
      ids: Seq[String],
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None,
  ): Future[TwitchResponse[TwitchClip]] = ~> {

    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")
    require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      query(
        "id"     -> ids,
        "before" -> before,
        "after"  -> after,
        "first"  -> size
      )
    }
  }

  /**
    * Acquires information about top clips for a specified broadcaster
    *
    * The response has a JSON payload with a data field containing an array of clip information elements and
    * a pagination field containing information required to query for more streams.
    *
    * @param broadcasterId ID of the broadcaster for whom top clips are returned
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch clip data
    */
  def byBroadcasterId(
      broadcasterId: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchClip]] = ~> {
    require(broadcasterId.nonEmpty, "Cannot query using empty broadcaster id")
    require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(clipsPath) {
      query(
        "broadcaster_id" -> Some(broadcasterId),
        "before"         -> before,
        "after"          -> after,
        "first"          -> size
      )
    }
  }

}
