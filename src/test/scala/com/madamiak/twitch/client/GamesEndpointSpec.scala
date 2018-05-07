package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import com.madamiak.twitch.model.api.{Game, JsonSupport, TwitchData}
import com.madamiak.twitch.model.{RateLimit, TwitchResponse}
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class GamesEndpointSpec extends AsyncWordSpec with Matchers with AsyncMockFactory with JsonSupport {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val executor: ExecutionContext      = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  "games endpoint" when {

    "performing a request to acquire games by id" should {

      "return single result" in {

        val query     = Query("id=1&id=2")
        val rateLimit = RateLimit(1, 2, 2)
        val game      = Game("493057", "gameA", "https://cdn.net/boxart/game-{width}x{height}.jpg")

        implicit val twitchClient: TwitchClient = mock[TwitchClient]
        (twitchClient
          .http[Game](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[Game]])) expects ("/helix/games", query, *) returns Future
          .successful(
            new TwitchResponse[Game](
              rateLimit,
              TwitchData(Seq(game))
            )
          )

        new GamesEndpoint().getGamesById(Seq(1, 2)).map { x =>
          x.rateLimit shouldEqual rateLimit
          x.twitchData.data.head shouldEqual game
          x.twitchData.pagination shouldBe None
        }
      }

      "fail when calling API without ids defined" in {
        implicit val twitchClient: TwitchClient = mock[TwitchClient]
        recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getGamesById(Seq()))
      }

      "fail when calling API with more ids than the limit" in {
        implicit val twitchClient: TwitchClient = mock[TwitchClient]
        recoverToSucceededIf[IllegalArgumentException](new GamesEndpoint().getGamesById(Seq.fill(101)(Random.nextInt)))
      }

    }

  }

}
