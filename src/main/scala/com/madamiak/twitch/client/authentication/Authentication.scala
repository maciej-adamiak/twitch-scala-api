package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }
import com.madamiak.twitch.TwitchConfiguration.config

import scala.concurrent.Future

trait Authentication {

  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  private[client] val clientId = config.getString("twitch.client.id")

  def recoverWhenUnauthorized(in: => Future[HttpResponse]): Future[HttpResponse]

  def authenticationHeader(): Future[HttpHeader]

}
