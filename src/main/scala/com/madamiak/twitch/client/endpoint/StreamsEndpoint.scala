package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.stream.{ TwitchStream, TwitchStreamMetadata }

import scala.concurrent.{ ExecutionContext, Future }

class StreamsEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  val streamsPath         = "/helix/streams"
  val streamsMetadataPath = "/helix/streams/metadata"

  /**
    * Gets information about active streams. Streams are returned sorted by number of current viewers, in descending order.
    * Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams
    *
    * @param communityIds Streams in a specified community ID
    * @param gameIds Streams broadcasting a specified game ID
    * @param languages Stream language
    * @param userIds Streams broadcast by one or more specified user ID
    * @param userLogins Streams broadcast by one or more specified user login names
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch stream data
    */
  def get(communityIds: Seq[String] = Seq(),
          gameIds: Seq[String] = Seq(),
          languages: Seq[String] = Seq(),
          userIds: Seq[String] = Seq(),
          userLogins: Seq[String] = Seq(),
          before: Option[String] = None,
          after: Option[String] = None,
          first: Option[Int] = None): Future[TwitchResponse[TwitchStream]] = ~> {

    require(communityIds.length <= 100, "Cannot query using more than 100 community ids")
    require(languages.length <= 100, "Cannot query using more than 100 languages")
    require(userIds.length <= 100, "Cannot query using more than 100 user ids")
    require(userLogins.length <= 100, "Cannot query using more than 100 user logins")
    require(gameIds.length <= 100, "Cannot query using more than 100 game ids")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(streamsPath) {
      query(
        "community_id" -> communityIds,
        "game_id"      -> gameIds,
        "language"     -> languages,
        "user_id"      -> userIds,
        "user_login"   -> userLogins,
        "before"       -> before,
        "after"        -> after,
        "first"        -> first
      )
    }
  }

  /**
    * Gets metadata information about active streams playing Overwatch or Hearthstone
    *
    * @param communityIds Returns streams in a specified community ID
    * @param gameIds Returns streams broadcasting the specified game ID
    * @param languages Stream language
    * @param userIds Returns streams broadcast by one or more of the specified user IDs
    * @param userLogins Returns streams broadcast by one or more of the specified user login names
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch stream metadata
    */
  def metadata(communityIds: Seq[String] = Seq(),
               gameIds: Seq[String] = Seq(),
               languages: Seq[String] = Seq(),
               userIds: Seq[String] = Seq(),
               userLogins: Seq[String] = Seq(),
               before: Option[String] = None,
               after: Option[String] = None,
               first: Option[Int] = None): Future[TwitchResponse[TwitchStreamMetadata]] = ~> {

    require(communityIds.length <= 100, "Cannot query using more than 100 community ids")
    require(languages.length <= 100, "Cannot query using more than 100 languages")
    require(userIds.length <= 100, "Cannot query using more than 100 user ids")
    require(userLogins.length <= 100, "Cannot query using more than 100 user logins")
    require(gameIds.length <= 100, "Cannot query using more than 100 game ids")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(streamsMetadataPath) {
      query(
        "community_id" -> communityIds,
        "game_id"      -> gameIds,
        "language"     -> languages,
        "user_id"      -> userIds,
        "user_login"   -> userLogins,
        "before"       -> before,
        "after"        -> after,
        "first"        -> first
      )
    }
  }

}
