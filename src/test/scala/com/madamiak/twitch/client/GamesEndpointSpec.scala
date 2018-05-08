package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import com.madamiak.twitch.model.api.{Game, JsonSupport, Pagination, TwitchData}
import com.madamiak.twitch.model.{RateLimit, TwitchResponse}
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class GamesEndpointSpec extends AsyncWordSpec with Matchers with AsyncMockFactory with JsonSupport {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val executor: ExecutionContext      = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  "games endpoint" which {

    "performs a request to acquire games by id" should {

      "succeed" when {

        "using valid query" in {

          val query      = Query("id=1&id=2")
          val rateLimit  = RateLimit(1, 2, 2)
          val game       = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")
          val twitchData = TwitchData(Seq(game))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games", query, *) returns Future
            .successful(
              new TwitchResponse[Game](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getGamesById(Seq(1, 2)).map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API without ids defined" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getGamesById(Seq()))
        }

        "calling API with more ids than the limit" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getGamesById(Seq.fill(101)(Random.nextInt))
          )
        }
      }

    }

    "performs a request to acquire games by name" should {

      "succeed" when {

        "using valid query" in {
          val query      = Query("name=gameA&name=gameB")
          val rateLimit  = RateLimit(1, 2, 2)
          val game       = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")
          val twitchData = TwitchData(Seq(game))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games", query, *) returns Future
            .successful(
              new TwitchResponse[Game](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getGamesByName(Seq("gameA", "gameB")).map(_.twitchData shouldEqual twitchData)
        }
      }

      "fail" when {
        "calling API without names defined" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getGamesByName(Seq()))
        }

        "calling API with more ids than the limit" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getGamesByName(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }

    }

    "performs a request to acquire top games" should {

      "succeed" when {

        "not using any parameters" in {
          val query      = Query()
          val rateLimit  = RateLimit(1, 2, 2)
          val game       = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")
          val pagination = Pagination("313")
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[Game](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getTopGames().map(_.twitchData shouldEqual twitchData)
        }

        "using single parameter" in {
          val query      = Query("before=313")
          val rateLimit  = RateLimit(1, 2, 2)
          val game       = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")
          val pagination = Pagination("313")
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[Game](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getTopGames(before = Some("313")).map(_.twitchData shouldEqual twitchData)
        }

        "using multiple parameters" in {
          val query      = Query("before=313&first=23")
          val rateLimit  = RateLimit(1, 2, 2)
          val game       = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")
          val pagination = Pagination("313")
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[Game](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint()
            .getTopGames(before = Some("313"), first = Some(23))
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "fetching more than 100 records" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getTopGames(first = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getTopGames(first = Some(-1))
          )
        }

      }

    }

  }

}
