package com.madamiak.twitch.client

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.model.api.{ Pagination, TwitchData, TwitchStream }
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.Future
import scala.util.Random

class StreamsEndpointTest extends EndpointWordSpec {

  val rateLimit  = RateLimit(1, 2, 2)
  val pagination = Pagination("313")
  val stream = TwitchStream(
    Seq("848d95be-90b3-44a5-b143-6e373754c382", "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"),
    "29307",
    "26007351216",
    "en",
    "2017-08-14T15:45:17Z",
    "https://static-cdn.jtvnw.net/previews-ttv/live_user_dansgaming-{width}x{height}.jpg",
    "[Punday Monday] Necromancer - Dan's First Character - Maps - !build",
    "live",
    "7236692",
    5723
  )

  "games endpoint" which {

    "performs a request to acquire streams" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query(
            "game_id=29307&language=en&language=pl&community_id=848d95be-90b3-44a5-b143-6e373754c382&community_id=fd0eab99-832a-4d7e-8cc0-04d73deb2e54&first=98"
          )
          val twitchData                          = TwitchData(Seq(stream))
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchStream](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchData[TwitchStream]])) expects ("/helix/streams", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchStream](
                rateLimit,
                twitchData
              )
            )
          new StreamsEndpoint()
            .getStreams(
              communityIds = Seq("848d95be-90b3-44a5-b143-6e373754c382", "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"),
              gameId = Seq("29307"),
              languages = Seq("en", "pl"),
              first = Some(98)
            )
            .map(_.twitchData shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new StreamsEndpoint().getStreams(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }
  }
}
