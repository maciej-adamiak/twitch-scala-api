package com.madamiak.twitch.client.endpoint

import java.net.URL
import java.util.UUID

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.stream.{ StreamType, TwitchStream, TwitchStreamMetadata }

import scala.util.Random

class StreamsEndpointSpec extends EndpointAsyncWordSpec {

  val stream = TwitchStream(
    Seq(
      UUID.fromString("848d95be-90b3-44a5-b143-6e373754c382"),
      UUID.fromString("fd0eab99-832a-4d7e-8cc0-04d73deb2e54")
    ),
    "29307",
    "26007351216",
    "en",
    dateFormatter.parse("2017-08-14T15:45:17Z"),
    new URL("https://static-cdn.jtvnw.net/previews-ttv/live_user_dansgaming-{width}x{height}.jpg"),
    "[Punday Monday] Necromancer - Dan's First Character - Maps - !build",
    StreamType.Live,
    "7236692",
    5723
  )
  val streamMetadata = TwitchStreamMetadata("488552", "23161357")

  "streams endpoint" which {

    "performs a request to acquire streams" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query(
            "community_id=848d95be-90b3-44a5-b143-6e373754c382&community_id=fd0eab99-832a-4d7e-8cc0-04d73deb2e54&game_id=29307&language=en&language=pl&first=98"
          )

          implicit val twitchClient: TwitchClient = twitchClientMock[TwitchStream]("/helix/streams", query, stream)
          new StreamsEndpoint()
            .get(
              communityIds = Seq("848d95be-90b3-44a5-b143-6e373754c382", "fd0eab99-832a-4d7e-8cc0-04d73deb2e54"),
              gameIds = Seq("29307"),
              languages = Seq("en", "pl"),
              first = Some(98)
            )
            .map(_.twitchPayload.data should contain only stream)
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
          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchStream]("/helix/streams/metadata", query, stream)
          new StreamsEndpoint()
            .metadata(
              gameIds = Seq("488552"),
              userIds = Seq("23161357"),
              first = Some(98)
            )
            .map(_.twitchPayload.data should contain only stream)
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
