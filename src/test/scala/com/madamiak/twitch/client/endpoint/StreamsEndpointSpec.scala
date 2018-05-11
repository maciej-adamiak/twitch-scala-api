package com.madamiak.twitch.client.endpoint

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.stream.{TwitchStream, TwitchStreamMetadata}
import com.madamiak.twitch.model.api.{Pagination, TwitchPayload}
import com.madamiak.twitch.model.{RateLimit, TwitchResponse}

import scala.concurrent.Future
import scala.util.Random

class StreamsEndpointSpec extends EndpointWordSpec {

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
  val streamMetadata = TwitchStreamMetadata("488552", "23161357")

  "games endpoint" which {

    "performs a request to acquire streams" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query(
            "game_id=29307&language=en&language=pl&community_id=848d95be-90b3-44a5-b143-6e373754c382&community_id=fd0eab99-832a-4d7e-8cc0-04d73deb2e54&first=98"
          )
          val twitchData                          = TwitchPayload(Seq(stream))
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchStream](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchStream]])) expects ("/helix/streams", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchStream](
                rateLimit,
                twitchData
              )
            )
          new StreamsEndpoint()
            .get(
              communityIds = Seq("848d95be-90b3-44a5-b143-6e373754c382", "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"),
              gameIds = Seq("29307"),
              languages = Seq("en", "pl"),
              first = Some(98)
            )
            .map(_.twitchPayload shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new StreamsEndpoint().get(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }

    "performs a request to acquire streams metadata" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query(
            "game_id=488552&user_id=23161357&first=98"
          )
          val twitchData                          = TwitchPayload(Seq(streamMetadata))
          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchStreamMetadata](_: String)(_: Query)(
              _: Unmarshaller[ResponseEntity, TwitchPayload[TwitchStreamMetadata]]
            )) expects ("/helix/streams/metadata", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchStreamMetadata](
                rateLimit,
                twitchData
              )
            )
          new StreamsEndpoint()
            .metadata(
              gameIds = Seq("488552"),
              userIds = Seq("23161357"),
              first = Some(98)
            )
            .map(_.twitchPayload shouldEqual twitchData)
        }

      }

      "fail" when {

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new StreamsEndpoint().metadata(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }
  }
}
