package com.madamiak.twitch.client.endpoint

import java.net.URL
import java.time.Duration

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.video.{ TwitchVideo, VideoType, VideoViewableType }

import scala.util.Random

class VideosEndpointSpec extends EndpointAsyncWordSpec {

  val video = TwitchVideo(
    "234482848",
    "en",
    dateFormatter.parse("2018-03-02T20:53:41Z"),
    new URL("https://www.twitch.tv/videos"),
    "-",
    new URL("https://www.twitch.tv/videos"),
    "32323",
    312,
    VideoViewableType.Private,
    VideoType.Archive,
    dateFormatter.parse("2018-03-02T20:53:41Z"),
    "desc",
    Duration.parse("PT3H8M33S")
  )

  "videos endpoint" which {

    "performs a request to acquire videos by id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("id=123&first=1")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchVideo]("/helix/videos", query, video)
          new VideosEndpoint()
            .byId("123")
            .map(_.twitchPayload.data should contain only video)
        }
      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().byId(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new VideosEndpoint().byId(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }

    "performs a request to acquire videos by user id" should {
      "succeed" when {

        "using a valid query" in {
          val query = Query("user_id=123")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchVideo]("/helix/videos", query, video)
          new VideosEndpoint()
            .byUserId("123")
            .map(_.twitchPayload.data should contain only video)
        }
      }

      "fail" when {

        "calling API without user id defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().byUserId(""))
        }
      }
    }

    "performs a request to acquire videos by game id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("game_id=123")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchVideo]("/helix/videos", query, video)
          new VideosEndpoint()
            .byGameId("123")
            .map(_.twitchPayload.data should contain only video)
        }
      }

      "fail" when {

        "calling API without game id defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().byGameId(""))
        }
      }
    }
  }
}
