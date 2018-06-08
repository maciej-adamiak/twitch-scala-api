package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }

import scala.concurrent.Future

/**
  * Provides basic Twitch authentication capabilities i.e using a client id
  */
trait ClientIdAuthentication extends Authentication {

  override def recoverWhenUnauthorized(in: => Future[HttpResponse]): Future[HttpResponse] = identity(in)

  override val authenticationHeader: Future[HttpHeader] = Future.successful(RawHeader("Client-ID", clientId))

}
