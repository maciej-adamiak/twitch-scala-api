package com.madamiak.twitch.client.endpoint

import java.net.URL

import akka.http.scaladsl.model.Uri.Query
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.user.{ BroadcasterType, TwitchFollow, TwitchUser, UserType }

import scala.util.Random

class UserEndpointSpec extends EndpointAsyncWordSpec {

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
          val query = Query("id=44322889")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchUser]("/helix/users", query, user)
          new UsersEndpoint()
            .getById(Seq("44322889"))
            .map(_.twitchPayload.data should contain only user)
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
          val query = Query("id=dallas")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchUser]("/helix/users", query, user)
          new UsersEndpoint()
            .getById(Seq("dallas"))
            .map(_.twitchPayload.data should contain only user)
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
          val query = Query("from_id=171003792")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchFollow]("/helix/users/follows", query, follow)
          new UsersEndpoint()
            .follows("171003792")
            .map(_.twitchPayload.data should contain only follow)
        }
      }
    }

    "performs a request to acquire users that follow a certain user" should {

      "succeed" when {

        "using a valid query" in {
          val query = Query("to_id=23161357")

          implicit val twitchClient: TwitchClient =
            twitchClientMock[TwitchFollow]("/helix/users/follows", query, follow)
          new UsersEndpoint()
            .followed("23161357")
            .map(_.twitchPayload.data should contain only follow)
        }
      }
    }
  }
}
