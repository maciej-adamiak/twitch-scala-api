package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.QueryUtils._
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.TwitchResponse
import com.madamiak.twitch.model.api.user.{ TwitchFollow, TwitchUser }

import scala.concurrent.{ ExecutionContext, Future }

class UsersEndpoint(
    implicit private[client] val context: ExecutionContext,
    implicit private[client] val client: TwitchClient
) extends Endpoint {

  private val usersPath   = "/helix/users"
  private val followsPath = "/helix/users/follows"

  /**
    * Gets information about one or more specified Twitch users
    *
    * @param ids user id
    * @return Twitch user data
    */
  def getById(ids: Seq[String]): Future[TwitchResponse[TwitchUser]] = ~> {
    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")

    client.http(usersPath) {
      query("id" -> ids)
    }

  }

  /**
    * Gets information about one or more specified Twitch users
    *
    * @param logins user login
    * @return Twitch user data
    */
  def getByLogin(logins: Seq[String]): Future[TwitchResponse[TwitchUser]] = ~> {
    require(logins.nonEmpty, "Cannot query using empty login list")
    require(logins.length <= 100, "Cannot query using more than 100 logins")

    client.http(usersPath) {
      query("login" -> logins)
    }
  }

  /**
    * Gets information on follow relationships between two Twitch users. Information returned is sorted in order, most recent follow first
    *
    * @param userId User ID. The request returns information about users who are being followed by the user.
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch user follow data
    */
  def follows(
      userId: String,
      after: Option[String] = None,
      first: Option[Int] = None
  ): Future[TwitchResponse[TwitchFollow]] = ~> {

    require(userId.nonEmpty, "Cannot query using empty user id")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(followsPath) {
      query(
        "from_id" -> userId,
        "after"   -> after,
        "first"   -> first
      )
    }
  }

  /**
    * Gets information on follow relationships between two Twitch users. Information returned is sorted in order, most recent follow first
    *
    * @param userId User ID. The request returns information about users who are following the user
    * @param after Cursor for forward pagination
    * @param first Maximum number of objects to return
    * @return Twitch user follow data
    */
  def followed(
      userId: String,
      after: Option[String] = None,
      first: Option[Int] = None
  ): Future[TwitchResponse[TwitchFollow]] = ~> {

    require(userId.nonEmpty, "Cannot query using empty user id")
    require(first.forall(_ > 0), "Cannot return less than a single clip in a one request")
    require(first.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

    client.http(followsPath) {
      query(
        "to_id" -> userId,
        "after" -> after,
        "first" -> first
      )
    }
  }

}
