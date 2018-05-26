package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils.query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.video.VideoPeriod.Period
import com.madamiak.twitch.model.api.video.VideoSort.Sort
import com.madamiak.twitch.model.api.video.VideoType.VideoType
import com.madamiak.twitch.model.api.video.{ TwitchVideo, VideoPeriod, VideoSort, VideoType }

import scala.concurrent.{ ExecutionContext, Future }

class VideosEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  private val videosPath = "/helix/videos"

  /**
    * Acquire video information by video id
    *
    * @param id ID of the video being queried
    * @return Twitch video
    */
  def byId(id: String): Future[TwitchResponse[TwitchVideo]] = by(Seq(id), size = Some(1))

  /**
    * Gets video information by video ids
    *
    * @param ids ID of the video being queried
    * @param period Period during which the video was created
    * @param sort Sort order of the videos
    * @param language Language of the video being queried
    * @param videoType Type of video
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @return Twitch video
    */
  def byId(
      ids: Seq[String],
      period: Period = VideoPeriod.All,
      sort: Sort = VideoSort.Time,
      language: Option[String] = None,
      videoType: VideoType = VideoType.All,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchVideo]] = ~> {
    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")

    by(ids, None, None, language, period, sort, videoType, before, after, size)
  }

  /**
    * Gets video information by user id
    *
    * @param userId ID of the user who owns the video
    * @param period Period during which the video was created
    * @param sort Sort order of the videos
    * @param language Language of the video being queried
    * @param videoType Type of video
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Number of values to be returned when getting videos by user or game ID
    * @return Twitch video
    */
  def byUserId(
      userId: String,
      language: Option[String] = None,
      period: Period = VideoPeriod.All,
      sort: Sort = VideoSort.Time,
      videoType: VideoType = VideoType.All,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchVideo]] = ~> {
    require(userId.nonEmpty, "Cannot query without defining a user id")

    by(Seq(), Option(userId), None, language, period, sort, videoType, before, after, size)
  }

  /**
    * Gets video information by game id
    *
    * @param gameId ID of the game the video is of
    * @param period Period during which the video was created
    * @param sort Sort order of the videos
    * @param language Language of the video being queried
    * @param videoType Type of video
    * @param before Cursor for backward pagination
    * @param after Cursor for forward pagination
    * @param size Number of values to be returned when getting videos by user or game ID
    * @return Twitch video
    */
  def byGameId(
      gameId: String,
      language: Option[String] = None,
      period: Period = VideoPeriod.All,
      sort: Sort = VideoSort.Time,
      videoType: VideoType = VideoType.All,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchVideo]] = ~> {
    require(gameId.nonEmpty, "Cannot query without defining a game id")

    by(Seq(), None, Option(gameId), language, period, sort, videoType, before, after, size)
  }

  private def by(
      ids: Seq[String] = Seq(),
      userId: Option[String] = None,
      gameId: Option[String] = None,
      language: Option[String] = None,
      period: Period = VideoPeriod.All,
      sort: Sort = VideoSort.Time,
      videoType: VideoType = VideoType.All,
      before: Option[String] = None,
      after: Option[String] = None,
      size: Option[Int] = None
  ): Future[TwitchResponse[TwitchVideo]] = ~> {
    require(ids.nonEmpty || userId.isDefined || gameId.isDefined,
            "One of parameters: ids, userId, gameId should be defined")
    require(size.forall(_ > 0), "Cannot return less than a single video in a one request")
    require(size.forall(_ <= 100), "Cannot return more than 100 videos in a one request")

    client.http(videosPath) {
      query(
        "id"       -> ids,
        "user_id"  -> userId,
        "game_id"  -> gameId,
        "period"   -> period,
        "sort"     -> sort,
        "type"     -> videoType,
        "language" -> language,
        "before"   -> before,
        "after"    -> after,
        "first"    -> size
      )
    }
  }
}
