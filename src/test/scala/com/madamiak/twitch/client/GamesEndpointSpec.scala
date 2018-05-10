package com.madamiak.twitch.client

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.model.api.game.TwitchGame
import com.madamiak.twitch.model.api.{ Pagination, TwitchData }
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.Future
import scala.util.Random

class GamesEndpointSpec extends EndpointWordSpec {

  val rateLimit  = RateLimit(1, 2, 2)
  val pagination = Pagination("313")
  val game       = TwitchGame("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")

  "games endpoint" which {

    "performs a request to acquire games by id" should {

      "succeed" when {

        "using a valid query" in {

          val query      = Query("id=123&id=312")
          val twitchData = TwitchData(Seq(game))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchGame](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchGame]])) expects ("/helix/games", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchGame](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getById(Seq("123", "312")).map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getById(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getById(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }

    }

    "performs a request to acquire games by name" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("name=gameA&name=gameB")
          val twitchData = TwitchData(Seq(game))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchGame](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchGame]])) expects ("/helix/games", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchGame](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().getByName(Seq("gameA", "gameB")).map(_.twitchData shouldEqual twitchData)
        }
      }

      "fail" when {
        "calling API without names defined" in {
          recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getByName(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().getByName(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }

    }

    "performs a request to acquire top games" should {

      "succeed" when {

        "not using any parameters" in {
          val query      = Query()
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchGame](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchGame]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchGame](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().popular().map(_.twitchData shouldEqual twitchData)
        }

        "using single parameter" in {
          val query      = Query("before=313")
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchGame](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchGame]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchGame](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint().popular(before = Some("313")).map(_.twitchData shouldEqual twitchData)
        }

        "using multiple parameters" in {
          val query      = Query("before=313&first=23")
          val twitchData = TwitchData(Seq(game), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchGame](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchGame]])) expects ("/helix/games/top", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchGame](
                rateLimit,
                twitchData
              )
            )
          new GamesEndpoint()
            .popular(before = Some("313"), first = Some(23))
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "trying to fetch more than 100 records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().popular(first = Some(101))
          )
        }

        "trying to fetch a negative number of records" in {
          recoverToSucceededIf[IllegalArgumentException](
            new GamesEndpoint().popular(first = Some(-1))
          )
        }

      }

    }

  }

}
