package com.madamiak.twitch.model.api

import java.net.URL
import java.text.SimpleDateFormat

import com.madamiak.twitch.model.api.clip.TwitchClip
import com.madamiak.twitch.model.api.game.TwitchGame
import org.scalatest.{ Matchers, WordSpec }
import spray.json._

class JsonSupportSpec extends WordSpec with Matchers with JsonSupport {

  val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  "json support" which {

    "supports unmarshalling" should {

      "extract game data" when {

        "processing valid values" in {

          val json =
            """
              |{
              |   "id":"493057",
              |   "name":"PLAYERUNKNOWN'S BATTLEGROUNDS",
              |   "box_art_url":"https://static-cdn.jtvnw.net"
              |}
            """.stripMargin

          val game = json.parseJson.convertTo[TwitchGame]

          game shouldEqual TwitchGame(
            "493057",
            "PLAYERUNKNOWN'S BATTLEGROUNDS",
            new URL("https://static-cdn.jtvnw.net")
          )
        }
      }

      "extract clip data" when {

        "processing valid values" in {

          val json =
            """
              |{
              |   "id":"RandomClip1",
              |   "url":"https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
              |   "embed_url":"https://clips.twitch.tv/embed?clip=RandomClip1",
              |   "broadcaster_id":"1234",
              |   "creator_id":"123456",
              |   "video_id":"1234567",
              |   "game_id":"33103",
              |   "language":"en",
              |   "title":"random1",
              |   "view_count":10,
              |   "created_at":"2017-11-30T22:34:18Z",
              |   "thumbnail_url":"https://clips-media.tv"
              |} 
            """.stripMargin

          val clip = json.parseJson.convertTo[TwitchClip]

          clip shouldEqual TwitchClip(
            "1234",
            dateFormatter.parse("2017-11-30T22:34:18Z"),
            "123456",
            new URL("https://clips.twitch.tv/embed?clip=RandomClip1"),
            "33103",
            "RandomClip1",
            "en",
            new URL("https://clips-media.tv"),
            "random1",
            new URL("https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage"),
            "1234567",
            10
          )
        }
      }

      "extract stream data" when {
        "processing valid values" in {}
      }

      "extract video data" when {
        "processing valid values" in {}
      }

    }

  }

}
