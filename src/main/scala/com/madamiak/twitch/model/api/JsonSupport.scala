package com.madamiak.twitch.model.api

import java.net.{ MalformedURLException, URL }
import java.text.{ ParseException, SimpleDateFormat }
import java.time.Duration
import java.time.format.DateTimeParseException
import java.util.{ Date, UUID }

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.madamiak.twitch.model.api.authentication.AuthenticationData
import com.madamiak.twitch.model.api.clip.TwitchClip
import com.madamiak.twitch.model.api.game.TwitchGame
import com.madamiak.twitch.model.api.stream.StreamType.StreamType
import com.madamiak.twitch.model.api.stream._
import com.madamiak.twitch.model.api.user.{ BroadcasterType, TwitchFollow, TwitchUser, UserType }
import com.madamiak.twitch.model.api.video.{ TwitchVideo, VideoType, VideoViewableType }
import spray.json.{ DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat }

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit def twitchPayloadFormat[T: JsonFormat]: RootJsonFormat[TwitchPayload[T]] = jsonFormat3(TwitchPayload[T])

  implicit val urlFormat: RootJsonFormat[URL] = new RootJsonFormat[URL] {

    override def read(json: JsValue): URL = json match {
      case JsString(s) =>
        try {
          new URL(s)
        } catch {
          case mue: MalformedURLException => throw DeserializationException(s"Invalid URL format $json", mue)
        }
      case _ => throw DeserializationException(s"Cannot read URL from a non-string type $json")
    }

    override def write(obj: URL): JsValue = JsString(obj.toString)
  }

  implicit val dateFormat: RootJsonFormat[Date] = new RootJsonFormat[Date] {
    val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    override def read(json: JsValue): Date = json match {
      case JsString(s) =>
        try {
          formatter.parse(s)
        } catch {
          case pe: ParseException => throw DeserializationException(s"Invalid date format $json", pe)
        }
      case _ => throw DeserializationException(s"Cannot read date from a non-string type $json")
    }

    override def write(obj: Date): JsValue = JsString(formatter.format(obj))
  }

  implicit val durationFormat: RootJsonFormat[Duration] = new RootJsonFormat[Duration] {
    override def write(obj: Duration): JsValue = JsString(obj.toString.replace("PT", "").toLowerCase)

    override def read(json: JsValue): Duration = json match {
      case JsString(s) =>
        try {
          Duration.parse(s"PT$s")
        } catch {
          case dtpe: DateTimeParseException => throw DeserializationException(s"Invalid duration format $json", dtpe)
        }
      case _ => throw DeserializationException(s"Cannot read duration from a non-string type $json")
    }
  }

  implicit val uuidFormat: RootJsonFormat[UUID] = new RootJsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(s) =>
        try {
          UUID.fromString(s)
        } catch {
          case iae: IllegalArgumentException => throw DeserializationException(s"Invalid UUID format $json", iae)
        }
      case _ => throw DeserializationException(s"Cannot read UUID from a non-string type $json")
    }
  }

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

  implicit val streamTypeFormat: RootJsonFormat[StreamType] = enumFormat(StreamType)

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

  implicit val overwatchHeroFormat: RootJsonFormat[OverwatchStreamHero]         = jsonFormat3(OverwatchStreamHero)
  implicit val overwatchPlayerFormat: RootJsonFormat[OverwatchStreamPlayer]     = jsonFormat1(OverwatchStreamPlayer)
  implicit val overwatchMetadataFormat: RootJsonFormat[OverwatchStreamMetadata] = jsonFormat1(OverwatchStreamMetadata)

  implicit val hearthstoneHeroFormat: RootJsonFormat[HearthstoneStreamHero] =
    jsonFormat(
      HearthstoneStreamHero,
      "type",
      "class",
      "name"
    )
  implicit val hearthstonePlayerFormat: RootJsonFormat[HearthstoneStreamPlayer] = jsonFormat1(HearthstoneStreamPlayer)
  implicit val hearthstoneMetadataFormat: RootJsonFormat[HearthstoneStreamMetadata] = jsonFormat2(
    HearthstoneStreamMetadata
  )

  implicit val twitchStreamMetadataFormat: RootJsonFormat[TwitchStreamMetadata] =
    jsonFormat(
      TwitchStreamMetadata,
      "game_id",
      "user_id",
      "hearthstone",
      "overwatch"
    )

  implicit val videoTypeFormat: RootJsonFormat[VideoType.Value]        = enumFormat(VideoType)
  implicit val viewTypeFormat: RootJsonFormat[VideoViewableType.Value] = enumFormat(VideoViewableType)

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

  implicit val broadcasterFormat: RootJsonFormat[BroadcasterType.Value] = enumFormat(BroadcasterType)
  implicit val userTypeFormat: RootJsonFormat[UserType.Value]           = enumFormat(UserType)

  implicit val twitchUserFormat: RootJsonFormat[TwitchUser] = jsonFormat(
    TwitchUser,
    "broadcaster_type",
    "description",
    "display_name",
    "email",
    "id",
    "login",
    "offline_image_url",
    "profile_image_url",
    "type",
    "view_count"
  )

  implicit val twitchFollowFormat: RootJsonFormat[TwitchFollow] = jsonFormat(
    TwitchFollow,
    "from_id",
    "to_id",
    "followed_at"
  )

  implicit val authenticationTokenFormat: RootJsonFormat[AuthenticationData] = jsonFormat(
    AuthenticationData,
    "access_token",
    "expires_in"
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
