package com.madamiak.twitch.client.endpoint

import akka.http.scaladsl.model.Uri
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.video.Period.Period
import com.madamiak.twitch.model.api.video.{ Period, Sort, VideoType }
import com.madamiak.twitch.model.api.video.Sort.Sort
import com.madamiak.twitch.model.api.video.VideoType.VideoType

import scala.concurrent.ExecutionContext

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
      Map(
        "id" -> ids
      ).query merge optionalParameters(period, sort, language, videoType, before, after, first)
    }
  }

  def getByUserId(userId: String,
                  language: Option[String] = None,
                  period: Period = Period.All,
                  sort: Sort = Sort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None) = ~> {
    
    client.http(videosPath) {
      Map(
        "user_id" -> userId
      ).query merge optionalParameters(period, sort, language, videoType, before, after, first)
    }
  }

  def getByGameId(gameId: String,
                  language: Option[String] = None,
                  period: Period = Period.All,
                  sort: Sort = Sort.Time,
                  videoType: VideoType = VideoType.All,
                  before: Option[String] = None,
                  after: Option[String] = None,
                  first: Option[Int] = None) = ~> {
    
    client.http(videosPath) {
      Map(
        "game_id" -> gameId
      ).query merge optionalParameters(period, sort, language, videoType, before, after, first)
    }
  }

  private def optionalParameters(period: Period,
                                 sort: Sort,
                                 language: Option[String],
                                 videoType: VideoType,
                                 before: Option[String],
                                 after: Option[String],
                                 first: Option[Int]) = {
    
    val requiredParameters = Map(
      "period" -> period,
      "sort"   -> sort,
      "type"   -> videoType
    ).query

    val optionalQuery = Map(
      "language" -> language,
      "before"   -> before,
      "after"    -> after,
      "first"    -> first
    ).query

    requiredParameters merge optionalQuery
  }

}
