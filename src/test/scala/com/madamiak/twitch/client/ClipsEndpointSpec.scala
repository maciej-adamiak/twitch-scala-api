package com.madamiak.twitch.client

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.model.api.clip.TwitchClip
import com.madamiak.twitch.model.api.{ Pagination, TwitchPayload }
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.Future
import scala.util.Random

class ClipsEndpointSpec extends EndpointWordSpec {

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
          val twitchData = TwitchPayload(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getById(Seq("AwkwardHelplessSalamanderSwiftRage"))
            .map(_.twitchPayload shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getById(Seq.fill(101)(Random.nextString(4)))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getById(Seq("AwkwardHelplessSalamanderSwiftRage"), first = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getById(Seq("AwkwardHelplessSalamanderSwiftRage"), first = Some(-1))
          )
        }

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getById(Seq())
          )
        }

      }

    }

    "performs a request to acquire clip data by broadcaster id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("game_id=123421")
          val twitchData = TwitchPayload(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getByGameId("123421")
            .map(_.twitchPayload shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API using empty game id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByGameId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByGameId("1234", first = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByGameId("1234", first = Some(101))
          )
        }

      }

    }

    "performs a request to acquire clip data by game id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("broadcaster_id=123421")
          val twitchData = TwitchPayload(Seq(clip), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchClip](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchClip]])) expects ("/helix/clips", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchClip](
                rateLimit,
                twitchData
              )
            )
          new ClipsEndpoint()
            .getByBroadcasterId("123421")
            .map(_.twitchPayload shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API using empty broadcaster id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByBroadcasterId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByBroadcasterId("1234", first = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().getByBroadcasterId("1234", first = Some(101))
          )
        }

      }

    }

  }

}
