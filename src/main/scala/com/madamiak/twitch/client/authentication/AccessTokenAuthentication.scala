package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.RawHeader

trait AccessTokenAuthentication extends Authentication {

  require(config.hasPath("twitch.client.secret"), "Twitch api client secret not defined")

  val fakeToken = "59ck4rwtgb96w42lul5jce8ia12gu1"

  // Acquire token
  override val authenticationHeader: HttpHeader = {
//    config.getString("twitch.client.id")
    RawHeader("Authorization", s"Bearer $fakeToken")
  }

}
