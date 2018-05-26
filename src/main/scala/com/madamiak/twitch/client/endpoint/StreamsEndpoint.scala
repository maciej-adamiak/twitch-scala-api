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
    * Acquire information regarding active streams in a specified community
    *
    * @param communityId Streams in a specified community ID
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def byCommunityId(
      communityId: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] =
    by(communityIds = Seq(communityId), before = before, after = after, size = size)

  /**
    * Acquire information regarding active streams related to a specified game
    *
    * @param gameId Streams broadcasting a specified game ID
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def byGameId(
      gameId: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] = by(gameIds = Seq(gameId), before = before, after = after, size = size)

  /**
    * Acquire information regarding active streams related to a specified user
    *
    * @param userId Streams broadcast by one or more specified user ID
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def byUserId(
      userId: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] = by(userIds = Seq(userId), before = before, after = after, size = size)

  /**
    * Acquire information regarding active streams related to a specified user
    *
    * @param userLogin Streams broadcast by one or more specified user login names
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def byUserLogin(
      userLogin: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] = by(userIds = Seq(userLogin), before = before, after = after, size = size)

  /**
    * Acquire information regarding active streams in a specified language
    *
    * @param language Stream language
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def byLanguage(
      language: String,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] = by(languages = Seq(language), before = before, after = after, size = size)

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
    * @param size Maximum number of objects to return
    * @return Twitch stream data
    */
  def by(
      communityIds: Seq[String] = Seq(),
      gameIds: Seq[String] = Seq(),
      languages: Seq[String] = Seq(),
      userIds: Seq[String] = Seq(),
      userLogins: Seq[String] = Seq(),
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchStream]] = ~> {

    require(communityIds.length <= 100, "Cannot query using more than 100 community ids")
    require(languages.length <= 100, "Cannot query using more than 100 languages")
    require(userIds.length <= 100, "Cannot query using more than 100 user ids")
    require(userLogins.length <= 100, "Cannot query using more than 100 user logins")
    require(gameIds.length <= 100, "Cannot query using more than 100 game ids")
    require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(streamsPath) {
      query(
        "community_id" -> communityIds,
        "game_id"      -> gameIds,
        "language"     -> languages,
        "user_id"      -> userIds,
        "user_login"   -> userLogins,
        "before"       -> before,
        "after"        -> after,
        "first"        -> size
      )
    }
  }

  val metadata: MetadataSegment.type = MetadataSegment

  object MetadataSegment {

    /**
      * Gets metadata information about active streams playing Overwatch or Hearthstone in a specified community
      *
      * @param communityId Stream community
      * @param before Cursor for backward pagination
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def byCommunityId(
        communityId: String,
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] =
      by(communityIds = Seq(communityId), before = before, after = after, size = size)

    /**
      * Gets metadata information about active streams playing Overwatch or Hearthstone related to a game
      *
      * @param gameId Streamed game id
      * @param before Cursor for backward pagination
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def byGameId(
        gameId: String,
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] =
      by(gameIds = Seq(gameId), before = before, after = after, size = size)

    /**
      * Gets metadata information about active streams playing Overwatch or Hearthstone by a specified user
      *
      * @param userId Stream owner id
      * @param before Cursor for backward pagination
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def byUserId(
        userId: String,
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] =
      by(userIds = Seq(userId), before = before, after = after, size = size)

    /**
      * Gets metadata information about active streams playing Overwatch or Hearthstone by a specified user
      *
      * @param userLogin Stream owner login
      * @param before Cursor for backward pagination
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def byUserLogin(
        userLogin: String,
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] =
      by(userLogins = Seq(userLogin), before = before, after = after, size = size)

    /**
      * Gets metadata information about active streams playing Overwatch or Hearthstone in a specified language
      *
      * @param language Stream language
      * @param before Cursor for backward pagination
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def byLanguage(
        language: String,
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] =
      by(languages = Seq(language), before = before, after = after, size = size)

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
      * @param size Maximum number of objects to return
      * @return Twitch stream metadata
      */
    def by(
        communityIds: Seq[String] = Seq(),
        gameIds: Seq[String] = Seq(),
        languages: Seq[String] = Seq(),
        userIds: Seq[String] = Seq(),
        userLogins: Seq[String] = Seq(),
        before: Option[String] = None,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchStreamMetadata]] = ~> {

      require(communityIds.length <= 100, "Cannot query using more than 100 community ids")
      require(languages.length <= 100, "Cannot query using more than 100 languages")
      require(userIds.length <= 100, "Cannot query using more than 100 user ids")
      require(userLogins.length <= 100, "Cannot query using more than 100 user logins")
      require(gameIds.length <= 100, "Cannot query using more than 100 game ids")
      require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

      client.http(streamsMetadataPath) {
        query(
          "community_id" -> communityIds,
          "game_id"      -> gameIds,
          "language"     -> languages,
          "user_id"      -> userIds,
          "user_login"   -> userLogins,
          "before"       -> before,
          "after"        -> after,
          "first"        -> size
        )
      }
    }
  }

}
