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
    * Acquire information regarding a specified Twitch user
    *
    * @param id user id
    * @return Twitch user data
    */
  def byId(id: String): Future[TwitchResponse[TwitchUser]] = byId(Seq(id))

  /**
    * Acquire information regarding one or more specified Twitch users
    *
    * @param ids user id
    * @return Twitch user data
    */
  def byId(ids: Seq[String]): Future[TwitchResponse[TwitchUser]] = ~> {
    require(ids.nonEmpty, "Cannot query using empty ids list")
    require(ids.length <= 100, "Cannot query using more than 100 ids")

    client.http(usersPath) {
      query("id" -> ids)
    }
  }

  /**
    * Acquire information regarding a specified Twitch user
    *
    * @param login user login
    * @return Twitch user data
    */
  def byLogin(login: String): Future[TwitchResponse[TwitchUser]] = byLogin(Seq(login))

  /**
    * Acquire information regarding one or more specified Twitch users
    *
    * @param logins user login
    * @return Twitch user data
    */
  def byLogin(logins: Seq[String]): Future[TwitchResponse[TwitchUser]] = ~> {
    require(logins.nonEmpty, "Cannot query using empty login list")
    require(logins.length <= 100, "Cannot query using more than 100 logins")

    client.http(usersPath) {
      query("login" -> logins)
    }
  }

  val follows: FollowsSegment.type = FollowsSegment

  object FollowsSegment {

    /**
      * Acquire information regarding follow relationships between two Twitch users.
      * Information returned is sorted in order, most recent follow first
      *
      * @param followerId User ID. The request returns information about users who are being followed by the user.
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch user follow data
      */
    def from(
        followerId: String,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchFollow]] = ~> {
      require(followerId.nonEmpty, "Cannot query using empty following user id")
      require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

      client.http(followsPath) {
        query(
          "from_id" -> followerId,
          "after"   -> after,
          "first"   -> size
        )
      }
    }

    /**
      * Acquire information regarding follow relationships between two Twitch users.
      * Information returned is sorted in order, most recent follow first
      *
      * @param followedId User ID. The request returns information about users who are following the user
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch user follow data
      */
    def to(
        followedId: String,
        after: Option[String] = None,
        size: Option[Int] = None
    ): Future[TwitchResponse[TwitchFollow]] = ~> {
      require(followedId.nonEmpty, "Cannot query using empty followed user id")
      require(size.forall(_ > 0), "Cannot return less than a single clip in a one request")
      require(size.forall(_ <= 100), "Cannot return more than 100 clips in a one request")

      client.http(followsPath) {
        query(
          "to_id" -> followedId,
          "after" -> after,
          "first" -> size
        )
      }
    }

    /**
      * Acquire information regarding follow relationships between two Twitch users.
      * Information returned is sorted in order, most recent follow first
      *
      * @param followedId User ID. The request returns information about users who are following the user
      * @param followerId User ID. The request returns information about users who are being followed by the user.
      * @param after Cursor for forward pagination
      * @param size Maximum number of objects to return
      * @return Twitch user follow data
      */
    def by(followedId: String,
           followerId: String,
           after: Option[String] = None,
           size: Option[Int] = None): Future[TwitchResponse[Boolean]] = ~> {
      client.http(followsPath) {
        query(
          "to_id"   -> followedId,
          "from_id" -> followerId,
          "after"   -> after,
          "first"   -> size
        )
      }
    }
  }
}
