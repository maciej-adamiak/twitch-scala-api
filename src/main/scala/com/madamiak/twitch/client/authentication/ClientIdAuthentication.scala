package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.RawHeader

trait ClientIdAuthentication extends Authentication {

  override val authenticationHeader: HttpHeader = RawHeader("Client-ID", clientId)

}
