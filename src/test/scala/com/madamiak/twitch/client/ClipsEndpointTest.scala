package com.madamiak.twitch.client

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.model.api.{ Pagination, TwitchClip, TwitchData }
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.Future
import scala.util.Random

class ClipsEndpointTest extends EndpointWordSpec {

  val rateLimit  = RateLimit(1, 2, 2)
  val pagination = Pagination("313")
  val clip = TwitchClip(
    "67955580",
    "2017-11-30T22:34:18Z",
    "53834192",
    "https://clips-media.tv",
    "488191",
    "AwkwardHelplessSalamanderSwiftRage",
    "en",
    "https://clips-media.tv",
    "babymetal",
    "https://clips.twitch.tv",
    "205586603",
    10
  )

  "clip endpoint" which {

    "performs a request to acquire clip data by id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("id=AwkwardHelplessSalamanderSwiftRage")
          val twitchData = TwitchData(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getClipsById(Seq("AwkwardHelplessSalamanderSwiftRage"))
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsById(Seq.fill(101)(Random.nextString(4)))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsById(Seq("AwkwardHelplessSalamanderSwiftRage"), first = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsById(Seq("AwkwardHelplessSalamanderSwiftRage"), first = Some(-1))
          )
        }

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsById(Seq())
          )
        }

      }

    }

    "performs a request to acquire clip data by broadcaster id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("game_id=123421")
          val twitchData = TwitchData(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getClipsByGameId("123421")
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API using empty game id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByGameId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByGameId("1234", first = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByGameId("1234", first = Some(101))
          )
        }

      }

    }

    "performs a request to acquire clip data by game id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("broadcaster_id=123421")
          val twitchData = TwitchData(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getClipsByBroadcasterId("123421")
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API using empty broadcaster id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByBroadcasterId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByBroadcasterId("1234", first = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getClipsByBroadcasterId("1234", first = Some(101))
          )
        }

      }

    }

  }

}
