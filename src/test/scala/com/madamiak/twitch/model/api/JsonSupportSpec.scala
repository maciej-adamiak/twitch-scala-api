package com.madamiak.twitch.model.api

import java.net.URL
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.UUID

import com.madamiak.twitch.model.api.JsonSupport._
import com.madamiak.twitch.model.api.clip.TwitchClip
import com.madamiak.twitch.model.api.game.TwitchGame
import com.madamiak.twitch.model.api.stream._
import com.madamiak.twitch.model.api.user.{ BroadcasterType, TwitchFollow, TwitchUser, UserType }
import com.madamiak.twitch.model.api.video.{ TwitchVideo, VideoType, VideoViewableType }
import org.scalatest.{ Matchers, WordSpec }
import spray.json._

class JsonSupportSpec extends WordSpec with Matchers {

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

        "processing valid values" in {
          val json =
            """
              |{
              |  "id":"26007494656",
              |  "user_id":"23161357",
              |  "game_id":"417752",
              |  "community_ids":[
              |    "5181e78f-2280-42a6-873d-758e25a7c313",
              |    "848d95be-90b3-44a5-b143-6e373754c382",
              |    "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"
              |  ],
              |  "type":"live",
              |  "title":"Hey Guys, It's Monday - Twitter: @Lirik",
              |  "viewer_count":32575,
              |  "started_at":"2017-08-14T16:08:32Z",
              |  "language":"en",
              |  "thumbnail_url":"https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg"
              |}
            """.stripMargin

          val stream = json.parseJson.convertTo[TwitchStream]

          stream shouldEqual TwitchStream(
            Seq(
              UUID.fromString("5181e78f-2280-42a6-873d-758e25a7c313"),
              UUID.fromString("848d95be-90b3-44a5-b143-6e373754c382"),
              UUID.fromString("fd0eab99-832a-4d7e-8cc0-04d73deb2e54")
            ),
            "417752",
            "26007494656",
            "en",
            dateFormatter.parse("2017-08-14T16:08:32Z"),
            new URL("https://static-cdn.jtvnw.net/previews-ttv/live_user_lirik-{width}x{height}.jpg"),
            "Hey Guys, It's Monday - Twitter: @Lirik",
            StreamType.Live,
            "23161357",
            32575
          )
        }
      }

      "extract stream metadata" when {

        "processing valid hearthstone values" in {
          val json =
            """
              |{
              |   "user_id":"1564968",
              |   "game_id":"138585",
              |   "overwatch":null,
              |   "hearthstone":{
              |      "broadcaster":{
              |         "hero":{
              |            "type":"Classic hero",
              |            "class":"Shaman",
              |            "name":"Thrall"
              |         }
              |      },
              |      "opponent":{
              |         "hero":{
              |            "type":"Classic hero",
              |            "class":"Warrior",
              |            "name":"Garrosh Hellscream"
              |         }
              |      }
              |   }
              |}
            """.stripMargin

          val metadata = json.parseJson.convertTo[TwitchStreamMetadata]

          metadata shouldEqual TwitchStreamMetadata(
            "138585",
            "1564968",
            hearthstoneData = Some(
              HearthstoneStreamMetadata(
                HearthstoneStreamPlayer(
                  Some(HearthstoneStreamHero("Classic hero", "Shaman", "Thrall"))
                ),
                HearthstoneStreamPlayer(
                  Some(HearthstoneStreamHero("Classic hero", "Warrior", "Garrosh Hellscream"))
                )
              )
            )
          )
        }

        "processing valid overwatch values" in {
          val json =
            """
              |{
              |   "user_id":"23161357",
              |   "game_id":"488552",
              |   "overwatch":{
              |      "broadcaster":{
              |         "hero":{
              |            "role":"Offense",
              |            "name":"Soldier 76",
              |            "ability":"Heavy Pulse Rifle"
              |         }
              |      }
              |   },
              |   "hearthstone":null
              |}
            """.stripMargin

          val metadata = json.parseJson.convertTo[TwitchStreamMetadata]

          metadata shouldEqual TwitchStreamMetadata(
            "488552",
            "23161357",
            overwatchData = Some(
              OverwatchStreamMetadata(
                OverwatchStreamPlayer(
                  Some(OverwatchStreamHero("Heavy Pulse Rifle", "Offense", "Soldier 76"))
                )
              )
            )
          )
        }
      }

      "extract video data" when {

        "processing valid values" in {
          val json =
            """
              |{
              |  "id":"234482848",
              |  "user_id":"67955580",
              |  "title":"-",
              |  "description":"",
              |  "created_at":"2018-03-02T20:53:41Z",
              |  "published_at":"2018-03-02T20:53:41Z",
              |  "url":"https://www.twitch.tv/videos/234482848",
              |  "thumbnail_url":"https://static-cdn.jtvnw.net/s3_vods",
              |  "viewable":"public",
              |  "view_count":142,
              |  "language":"en",
              |  "type":"archive",
              |  "duration":"3h8m33s"
              |}
            """.stripMargin

          val video = json.parseJson.convertTo[TwitchVideo]

          video shouldEqual TwitchVideo(
            "234482848",
            "en",
            dateFormatter.parse("2018-03-02T20:53:41Z"),
            new URL(
              "https://static-cdn.jtvnw.net/s3_vods"
            ),
            "-",
            new URL("https://www.twitch.tv/videos/234482848"),
            "67955580",
            142,
            VideoViewableType.Public,
            VideoType.Archive,
            dateFormatter.parse("2018-03-02T20:53:41Z"),
            "",
            Duration.parse("PT3H8M33S")
          )
        }
      }

      "extract user data" when {

        "processing valid values" in {
          val json =
            """
              |{
              |   "id":"44322889",
              |   "login":"dallas",
              |   "display_name":"dallas",
              |   "type":"staff",
              |   "broadcaster_type":"",
              |   "description":"Just a gamer playing games and chatting. :)",
              |   "profile_image_url":"https://static-cdn.jtvnw.net/jtv_user_picturesA",
              |   "offline_image_url":"https://static-cdn.jtvnw.net/jtv_user_picturesB",
              |   "view_count":191836881,
              |   "email":"login@provider.com"
              |}
            """.stripMargin

          val user = json.parseJson.convertTo[TwitchUser]

          user shouldEqual TwitchUser(
            BroadcasterType.Undefined,
            "Just a gamer playing games and chatting. :)",
            "dallas",
            "login@provider.com",
            "44322889",
            "dallas",
            new URL("https://static-cdn.jtvnw.net/jtv_user_picturesB"),
            new URL("https://static-cdn.jtvnw.net/jtv_user_picturesA"),
            UserType.Staff,
            191836881
          )
        }
      }

      "extract user follow data" when {

        "processing valid values" in {
          val json =
            """
              |{
              |   "from_id":"171003792",
              |   "to_id":"23161357",
              |   "followed_at":"2017-08-22T22:55:24Z"
              |}
            """.stripMargin

          val follow = json.parseJson.convertTo[TwitchFollow]

          follow shouldEqual TwitchFollow(
            "171003792",
            "23161357",
            dateFormatter.parse("2017-08-22T22:55:24Z")
          )
        }
      }
    }
  }
}
