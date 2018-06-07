package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.{ HttpHeader, HttpResponse, StatusCode }

import scala.concurrent.Future

trait Authentication {

  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  private[client] val clientId = config.getString("twitch.client.id")

  def recovery(in: Future[(StatusCode, HttpResponse)]): Future[(StatusCode, HttpResponse)]

  def authenticate(): Future[HttpHeader]

}
