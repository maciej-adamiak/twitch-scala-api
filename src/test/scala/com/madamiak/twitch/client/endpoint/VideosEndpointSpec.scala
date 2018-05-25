package com.madamiak.twitch.client.endpoint

import java.net.URL
import java.time.Duration

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.video.{TwitchVideo, VideoType, VideoViewableType}
import com.madamiak.twitch.model.api.{Pagination, TwitchPayload}
import com.madamiak.twitch.model.{RateLimit, TwitchResponse}

import scala.concurrent.Future
import scala.util.Random

class VideosEndpointSpec extends EndpointAsyncWordSpec {

  val rateLimit  = RateLimit(1, 2, 2)
  val pagination = Pagination("313")
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
          val query      = Query("id=123&id=312")
          val twitchData = TwitchPayload(Seq(video))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchVideo](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchVideo]])) expects ("/helix/videos", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchVideo](
                rateLimit,
                twitchData
              )
            )
          new VideosEndpoint().getByIds(Seq("123", "312")).map(_.twitchPayload shouldEqual twitchData)
        }
      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().getByIds(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new VideosEndpoint().getByIds(Seq.fill(101)(Random.nextString(4)))
          )
        }

      }
    }

    "performs a request to acquire videos by user id" should {
      "succeed" when {

        "using a valid query" in {
          val query      = Query("user_id=123")
          val twitchData = TwitchPayload(Seq(video))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchVideo](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchVideo]])) expects ("/helix/videos", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchVideo](
                rateLimit,
                twitchData
              )
            )
          new VideosEndpoint().getByUserId("123").map(_.twitchPayload shouldEqual twitchData)
        }
      }

      "fail" when {

        "calling API without user id defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().getByUserId(null))
        }

      }
    }

    "performs a request to acquire videos by game id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("game_id=123")
          val twitchData = TwitchPayload(Seq(video))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchVideo](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchVideo]])) expects ("/helix/videos", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchVideo](
                rateLimit,
                twitchData
              )
            )
          new VideosEndpoint().getByGameId("123").map(_.twitchPayload shouldEqual twitchData)
        }
      }

      "fail" when {

        "calling API without game id defined" in {
          recoverToSucceededIf[IllegalArgumentException](new VideosEndpoint().getByGameId(null))
        }

      }
    }

  }

}
