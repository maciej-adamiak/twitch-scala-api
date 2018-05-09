package com.madamiak.twitch.client

import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.Stream

import scala.concurrent.{ ExecutionContext, Future }

class StreamsEndpoint(implicit val context: ExecutionContext, implicit val client: TwitchClient) extends Endpoint {

  val streamsPath = "/streams"

  /**
    * Gets information about active streams. Streams are returned sorted by number of current viewers, in descending order.
    * Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams
    *
    * @param communityId streams in a specified community ID
    * @param gameId streams broadcasting a specified game ID
    * @param languages stream language
    * @param userId streams broadcast by one or more specified user ID
    * @param userLogin streams broadcast by one or more specified user login names
    * @param before cursor for backward pagination
    * @param after cursor for forward pagination
    * @param first maximum number of objects to return
    * @return Twitch stream data
    */
  def get(communityIds: Seq[String] = Seq(),
          gameId: Seq[String] = Seq(),
          languages: Seq[String] = Seq(),
          userIds: Seq[String] = Seq(),
          userLogins: Seq[String] = Seq(),
          before: Option[String] = None,
          after: Option[String] = None,
          first: Option[Int] = None): Future[TwitchResponse[Stream]] =
    Future {
      require(communityIds.length <= 100, "Cannot query using more than 100 community ids")
      require(languages.length <= 100, "Cannot query using more than 100 languages")
      require(userIds.length <= 100, "Cannot query using more than 100 user ids")
      require(userLogins.length <= 100, "Cannot query using more than 100 user logins")
      require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")
    }.flatMap(
      _ =>
        client.http(streamsPath) {
          val queryParameters = Map(
            "community_id" -> communityIds,
            "game_id"      -> gameId,
            "language"     -> languages,
            "user_id"      -> userIds,
            "user_login"   -> userLogins
          ).query

          val paginationParameters = Map(
            "before" -> before,
            "after"  -> after,
            "first"  -> first
          ).query

          queryParameters + paginationParameters
      }
    )

}
