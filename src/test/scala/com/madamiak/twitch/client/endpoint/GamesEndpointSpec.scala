package com.madamiak.twitch.client.endpoint

import java.net.URL

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.game.TwitchGame

import scala.util.Random

class GamesEndpointSpec extends EndpointAsyncWordSpec {

  val game = TwitchGame(
    "493057",
    "gameA",
    new URL("https://cdn.net/boxart/game-{width}x{height}.jpg")
  )

  "games endpoint" which {

    "performs a request to acquire games by id" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("id=123&id=312")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchGame]("/helix/games", query, game)
          new GamesEndpoint()
            .byId("123", "312")
            .map(_.twitchPayload.data should contain only game)
        }
      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().byId())
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().byId(Seq.fill(101)(Random.nextString(4)): _*)
          )
        }
      }
    }

    "performs a request to acquire games by name" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("name=gameA&name=gameB")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchGame]("/helix/games", query, game)
          new GamesEndpoint()
            .byName("gameA", "gameB")
            .map(_.twitchPayload.data should contain only game)
        }
      }

      "fail" when {
        "calling API without names defined" in {
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().byName())
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().byName(Seq.fill(101)(Random.nextString(4)): _*)
          )
        }
      }
    }

    "performs a request to acquire top games" should {

      "succeed" when {

        "not using any parameters" in {
          val query = Query()

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchGame]("/helix/games/top", query, game)
          new GamesEndpoint()
            .popular()
            .map(_.twitchPayload.data should contain only game)
        }

        "using single parameter" in {
          val query = Query("before=313")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchGame]("/helix/games/top", query, game)
          new GamesEndpoint()
            .popular(before = Some("313"))
            .map(_.twitchPayload.data should contain only game)
        }

        "using multiple parameters" in {
          val query = Query("before=313&first=23")

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchGame]("/helix/games/top", query, game)
          new GamesEndpoint()
            .popular(before = Some("313"), size = Some(23))
            .map(_.twitchPayload.data should contain only game)
        }
      }

      "fail" when {

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().popular(size = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().popular(size = Some(-1))
          )
        }
      }
    }
  }
}
