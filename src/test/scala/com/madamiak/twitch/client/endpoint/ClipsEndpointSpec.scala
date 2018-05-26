package com.madamiak.twitch.client.endpoint

import java.net.URL

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.clip.TwitchClip

import scala.util.Random

class ClipsEndpointSpec extends EndpointAsyncWordSpec {

  val clip = TwitchClip(
    "67955580",
    dateFormatter.parse("2017-11-30T22:34:18Z"),
    "53834192",
    new URL("https://clips-media.tv"),
    "488191",
    "AwkwardHelplessSalamanderSwiftRage",
    "en",
    new URL("https://clips-media.tv"),
    "babymetal",
    new URL("https://clips.twitch.tv"),
    "205586603",
    10
  )

  "clips endpoint" which {

    "performs a request to acquire clip data by id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("id=AwkwardHelplessSalamanderSwiftRage")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchClip]("/helix/clips", query, clip)
          new ClipsEndpoint()
            .byId(Seq("AwkwardHelplessSalamanderSwiftRage"))
            .map(_.twitchPayload.data should contain only clip)
        }
      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byId(Seq.fill(101)(Random.nextString(4)))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byId(Seq("AwkwardHelplessSalamanderSwiftRage"), size = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byId(Seq("AwkwardHelplessSalamanderSwiftRage"), size = Some(-1))
          )
        }

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byId(Seq())
          )
        }
      }
    }

    "performs a request to acquire clip data by broadcaster id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("game_id=123421")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchClip]("/helix/clips", query, clip)
          new ClipsEndpoint()
            .byGameId("123421")
            .map(_.twitchPayload.data should contain only clip)
        }
      }

      "fail" when {

        "calling API using empty game id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byGameId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byGameId("1234", size = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byGameId("1234", size = Some(101))
          )
        }
      }
    }

    "performs a request to acquire clip data by game id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("broadcaster_id=123421")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchClip]("/helix/clips", query, clip)
          new ClipsEndpoint()
            .byBroadcasterId("123421")
            .map(_.twitchPayload.data should contain only clip)
        }
      }

      "fail" when {

        "calling API using empty broadcaster id" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byBroadcasterId("")
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byBroadcasterId("1234", size = Some(-1))
          )
        }

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new ClipsEndpoint().byBroadcasterId("1234", size = Some(101))
          )
        }
      }
    }
  }
}
