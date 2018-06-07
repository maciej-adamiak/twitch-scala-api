package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }

import scala.concurrent.Future

trait ClientIdAuthentication extends Authentication {

  override def recovery(in: Future[HttpResponse]): Future[HttpResponse] = identity(in)

  override val authenticate: Future[HttpHeader] = Future.successful(RawHeader("Client-ID", clientId))

}
