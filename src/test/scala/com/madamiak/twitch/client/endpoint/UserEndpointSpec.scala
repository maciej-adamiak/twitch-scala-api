package com.madamiak.twitch.client.endpoint

import java.net.URL

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.user.{ BroadcasterType, TwitchFollow, TwitchUser, UserType }
import com.madamiak.twitch.model.api.{ Pagination, TwitchPayload }
import com.madamiak.twitch.model.{ RateLimit, TwitchResponse }

import scala.concurrent.Future
import scala.util.Random

class UserEndpointSpec extends EndpointAsyncWordSpec {

  val rateLimit  = RateLimit(1, 2, 2)
  val pagination = Pagination("313")

  val user = TwitchUser(
    BroadcasterType.Undefined,
    "Just a gamer playing games and chatting. :)",
    "dallas",
    "login@provider.com",
    "44322889",
    "dallas",
    new URL("https://static-cdn.jtvnw.net/jtv_user_picturesB"),
    new URL("https://static-cdn.jtvnw.net/jtv_user_picturesA"),
    UserType.Staff,
    191836881
  )

  val follow = TwitchFollow(
    "171003792",
    "23161357",
    dateFormatter.parse("2017-08-22T22:55:24Z")
  )

  "users endpoint" which {

    "performs a request to acquire user data by user id" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("id=44322889")
          val twitchData = TwitchPayload(Seq(user), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchUser](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchUser]])) expects ("/helix/users", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchUser](
                rateLimit,
                twitchData
              )
            )
          new UsersEndpoint()
            .getById(Seq("44322889"))
            .map(_.twitchPayload shouldEqual twitchData)
        }
      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new UsersEndpoint().getById(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new UsersEndpoint().getById(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }

    "performs a request to acquire user data by login" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("id=dallas")
          val twitchData = TwitchPayload(Seq(user), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchUser](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchUser]])) expects ("/helix/users", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchUser](
                rateLimit,
                twitchData
              )
            )
          new UsersEndpoint()
            .getById(Seq("dallas"))
            .map(_.twitchPayload shouldEqual twitchData)
        }
      }

      "fail" when {

        "calling API without ids defined" in {
          recoverToSucceededIf[IllegalArgumentException](new UsersEndpoint().getById(Seq()))
        }

        "calling API with more ids than the limit" in {
          recoverToSucceededIf[IllegalArgumentException](
            new UsersEndpoint().getById(Seq.fill(101)(Random.nextString(4)))
          )
        }
      }
    }

    "performs a request to acquire users followed by a certain user" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("from_id=171003792")
          val twitchData = TwitchPayload(Seq(follow), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchFollow](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchFollow]])) expects ("/helix/users/follows", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchFollow](
                rateLimit,
                twitchData
              )
            )
          new UsersEndpoint()
            .follows("171003792")
            .map(_.twitchPayload shouldEqual twitchData)
        }
      }
    }

    "performs a request to acquire users that follow a certain user" should {

      "succeed" when {

        "using a valid query" in {
          val query      = Query("to_id=23161357")
          val twitchData = TwitchPayload(Seq(follow), Some(pagination))

          implicit val twitchClient: TwitchClient = mock[TwitchClient]
          (twitchClient
            .http[TwitchFollow](_: String)(_: Query)(_: Unmarshaller[ResponseEntity, TwitchPayload[TwitchFollow]])) expects ("/helix/users/follows", query, *) returns Future
            .successful(
              new TwitchResponse[TwitchFollow](
                rateLimit,
                twitchData
              )
            )
          new UsersEndpoint()
            .followed("23161357")
            .map(_.twitchPayload shouldEqual twitchData)
        }
      }
    }
  }
}
