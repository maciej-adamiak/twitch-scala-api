package com.madamiak.twitch.model.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.madamiak.twitch.model.api.clip.TwitchClip
import com.madamiak.twitch.model.api.game.TwitchGame
import com.madamiak.twitch.model.api.stream._
import com.madamiak.twitch.model.api.video.{TwitchVideo, VideoType, ViewType}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit def twitchDataFormat[T: JsonFormat]: RootJsonFormat[TwitchPayload[T]] = jsonFormat2(TwitchPayload[T])

  implicit val gameFormat: RootJsonFormat[TwitchGame] = jsonFormat(
    TwitchGame,
    "id",
    "name",
    "box_art_url"
  )

  implicit val clipFormat: RootJsonFormat[TwitchClip] = jsonFormat(
    TwitchClip,
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
    "view_count"
  )

  implicit val paginationFormat: RootJsonFormat[Pagination] = jsonFormat1(Pagination)

  implicit val streamFormat: RootJsonFormat[TwitchStream] = jsonFormat(
    TwitchStream,
    "community_ids",
    "game_id",
    "id",
    "language",
    "started_at",
    "thumbnail_url",
    "title",
    "type",
    "user_id",
    "viewer_count"
  )

  implicit val overwatchHeroFormat: RootJsonFormat[OverwatchHero]         = jsonFormat3(OverwatchHero)
  implicit val overwatchPlayerFormat: RootJsonFormat[OverwatchPlayer]     = jsonFormat1(OverwatchPlayer)
  implicit val overwatchMetadataFormat: RootJsonFormat[OverwatchMetadata] = jsonFormat1(OverwatchMetadata)

  implicit val hearthstoneHeroFormat: RootJsonFormat[HearthstoneHero] =
    jsonFormat(
      HearthstoneHero,
      "type",
      "class",
      "type"
    )
  implicit val hearthstonePlayerFormat: RootJsonFormat[HearthstonePlayer]     = jsonFormat1(HearthstonePlayer)
  implicit val hearthstoneMetadataFormat: RootJsonFormat[HearthstoneMetadata] = jsonFormat2(HearthstoneMetadata)

  implicit val twitchStreamMetadataFormat: RootJsonFormat[TwitchStreamMetadata] =
    jsonFormat(
      TwitchStreamMetadata,
      "game_id",
      "user_id",
      "hearthstone",
      "overwatch"
    )

  implicit val videoTypeFormat: RootJsonFormat[VideoType.Value] = enumFormat(VideoType)
  implicit val viewTypeFormat: RootJsonFormat[ViewType.Value]   = enumFormat(ViewType)

  implicit val videoFormat: RootJsonFormat[TwitchVideo] = jsonFormat(
    TwitchVideo,
    "id",
    "language",
    "published_at",
    "thumbnail_url",
    "title",
    "url",
    "user_id",
    "view_count",
    "viewable",
    "type",
    "created_at",
    "description",
    "duration"
  )

  /**
    * Custom enum format
    *
    * @param enumeration formatted enumeration
    * @tparam E enumeration type
    * @return Akka spray json format
    */
  private def enumFormat[E <: Enumeration](enumeration: E): RootJsonFormat[E#Value] =
    new RootJsonFormat[E#Value] {
      def read(json: JsValue): E#Value = json match {
        case JsString(txt) => enumeration.withName(txt)
        case _             => throw DeserializationException(s"Cannot format $enumeration")
      }
      def write(obj: E#Value): JsValue = JsString(obj.toString)
    }
}
