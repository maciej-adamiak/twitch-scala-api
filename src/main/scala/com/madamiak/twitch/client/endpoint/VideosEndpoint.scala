package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils.query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.video.VideoPeriod.Period
import com.madamiak.twitch.model.api.video.VideoSort.Sort
import com.madamiak.twitch.model.api.video.VideoType.VideoType
import com.madamiak.twitch.model.api.video.{TwitchVideo, VideoPeriod, VideoSort, VideoType}

import scala.concurrent.{ExecutionContext, Future}

class VideosEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  val videosPath = "/helix/videos"

  /**
    * Gets video information by video id
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
  def getByIds(ids: Seq[String],
               period: Period = VideoPeriod.All,
               sort: Sort = VideoSort.Time,
               language: Option[String] = None,
               videoType: VideoType = VideoType.All,
               before: Option[String] = None,
               after: Option[String] = None,
               first: Option[Int] = None): Future[TwitchResponse[TwitchVideo]] = ~> {

    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 names")

    client.http(videosPath) {
      query(
        "id"       -> ids,
        "period"   -> period,
        "sort"     -> sort,
        "type"     -> videoType,
        "language" -> language,
        "before"   -> before,
        "after"    -> after,
        "first"    -> first
      )
    }
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
    * @param first Number of values to be returned when getting videos by user or game ID
    * @return Twitch video
    */
  def getByUserId(userId: String,
                  language: Option[String] = None,
                  period: Period = VideoPeriod.All,
                  sort: Sort = VideoSort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchVideo]] = ~> {

    require(userId != null && userId.nonEmpty, "Cannot query without defining a user id")
    require(first.forall(_ > 0), "Cannot return less than a single video in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 videos in a one request")

    client.http(videosPath) {
      query(
        "user_id"  -> userId,
        "period"   -> period,
        "sort"     -> sort,
        "type"     -> videoType,
        "language" -> language,
        "before"   -> before,
        "after"    -> after,
        "first"    -> first
      )
    }
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
    * @param first Number of values to be returned when getting videos by user or game ID
    * @return Twitch video
    */
  def getByGameId(gameId: String,
                  language: Option[String] = None,
                  period: Period = VideoPeriod.All,
                  sort: Sort = VideoSort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchVideo]] = ~> {

    require(gameId != null && gameId.nonEmpty, "Cannot query without defining a game id")
    require(first.forall(_ > 0), "Cannot return less than a single video in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 videos in a one request")

    client.http(videosPath) {
      query(
        "game_id"  -> gameId,
        "period"   -> period,
        "sort"     -> sort,
        "type"     -> videoType,
        "language" -> language,
        "before"   -> before,
        "after"    -> after,
        "first"    -> first
      )
    }
  }

}
