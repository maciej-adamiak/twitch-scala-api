package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.RawHeader

trait ClientIdAuthentication extends Authentication {

  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  override val authenticationHeader: HttpHeader = RawHeader("Client-ID", config.getString("twitch.client.id"))

}
