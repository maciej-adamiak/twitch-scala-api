package com.madamiak.twitch.model.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ DefaultJsonProtocol, JsonFormat, RootJsonFormat }

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit def twitchDataFormat[T: JsonFormat]: RootJsonFormat[TwitchData[T]] = jsonFormat2(TwitchData[T])

  implicit val gameFormat: RootJsonFormat[TwitchGame] = jsonFormat(TwitchGame, "id", "name", "box_art_url")

  implicit val clipFormat: RootJsonFormat[TwitchClip] = jsonFormat(TwitchClip,
                                                                   "broadcaster_id",
                                                                   "created_at",
                                                                   "creator_id",
                                                                   "embed_url",
                                                                   "game_id",
                                                                   "id",
                                                                   "language",
                                                                   "thumbnail_url",
                                                                   "title",
                                                                   "url",
                                                                   "video_id",
                                                                   "view_count")

  implicit val paginationFormat: RootJsonFormat[Pagination] = jsonFormat1(Pagination)

  implicit val streamFormat: RootJsonFormat[TwitchStream] = jsonFormat(TwitchStream,
                                                                       "community_ids",
                                                                       "game_id",
                                                                       "id",
                                                                       "language",
                                                                       "started_at",
                                                                       "thumbnail_url",
                                                                       "title",
                                                                       "type",
                                                                       "user_id",
                                                                       "viewer_count")

}
