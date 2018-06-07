package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }

import scala.concurrent.Future

trait Authentication {

  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  private[client] val clientId = config.getString("twitch.client.id")

  //TODO maybe another trait or betern name
  def recovery(in: => Future[HttpResponse]): Future[HttpResponse]

  def authenticate(): Future[HttpHeader]

}
