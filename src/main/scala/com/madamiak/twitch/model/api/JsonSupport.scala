package com.madamiak.twitch.model.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit def twitchDataFormat[T: JsonFormat]: RootJsonFormat[TwitchData[T]] = jsonFormat2(TwitchData[T])

  implicit val gameFormat: RootJsonFormat[Game] = jsonFormat(Game, "id", "name", "box_art_url")

  implicit val clipFormat: RootJsonFormat[Clip] = jsonFormat(Clip,
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

}
