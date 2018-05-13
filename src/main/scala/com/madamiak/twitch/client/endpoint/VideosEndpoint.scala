package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils.query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.video.Period.Period
import com.madamiak.twitch.model.api.video.Sort.Sort
import com.madamiak.twitch.model.api.video.VideoType.VideoType
import com.madamiak.twitch.model.api.video.{Period, Sort, TwitchVideo, VideoType}

import scala.concurrent.{ExecutionContext, Future}

//TODO
class VideosEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  val videosPath = "/helix/videos"

  def getByIds(ids: Seq[String],
               period: Period = Period.All,
               sort: Sort = Sort.Time,
               language: Option[String] = None,
               videoType: VideoType = VideoType.All,
               before: Option[String] = None,
               after: Option[String] = None,
               first: Option[Int] = None) = ~> {

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

  def getByUserId(userId: String,
                  language: Option[String] = None,
                  period: Period = Period.All,
                  sort: Sort = Sort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchVideo]] = ~> {

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

  def getByGameId(gameId: String,
                  language: Option[String] = None,
                  period: Period = Period.All,
                  sort: Sort = Sort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None): Future[TwitchResponse[TwitchVideo]] = ~> {

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
