package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.RawHeader

import scala.concurrent.Future

trait ClientIdAuthentication extends Authentication {

  override val authenticate: Future[HttpHeader] = Future.successful(RawHeader("Client-ID", clientId))

}
