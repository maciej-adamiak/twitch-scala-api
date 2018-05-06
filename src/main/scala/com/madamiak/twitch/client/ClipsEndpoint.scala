package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.stream.ActorMaterializer
import com.madamiak.twitch.model.{Clip, TwitchData}

import scala.concurrent.{ExecutionContext, Future}

class ClipsEndpoint(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer
) {

  private val clipsPath = "/helix/clips"

  private val client = new TwitchClient()

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
  def getClipsByGameId(gameId: Long,
                       before: Option[String] = None,
                       after: Option[String] = None,
                       first: Option[Int] = None): Future[TwitchData[Clip]] = client.http(clipsPath) {
    Query {
      Map(
        "game_id" -> Some(gameId),
        "before"  -> before,
        "after"   -> after,
        "first"   -> first
      ).filter(_._2.isDefined).mapValues(_.get.toString)
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
  def getClipsById(ids: Seq[String],
                   before: Option[String] = None,
                   after: Option[String] = None,
                   first: Option[Int] = None): Future[TwitchData[Clip]] = client.http(clipsPath) {
    Query {
      Map(
        "before" -> before,
        "after"  -> after,
        "first"  -> first
      ).filter(_._2.isDefined).mapValues(_.get.toString) ++ ids.map("id" -> _)
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
  def getClipsByBroadcasterId(broadcasterId: Long,
                              before: Option[String] = None,
                              after: Option[String] = None,
                              first: Option[Int] = None): Future[TwitchData[Clip]] = client.http(clipsPath) {
    Query {
      Map(
        "broadcaster_id" -> Some(broadcasterId),
        "before"         -> before,
        "after"          -> after,
        "first"          -> first
      ).filter(_._2.isDefined).mapValues(_.get.toString)
    }
  }

}