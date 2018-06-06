package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader

trait Authentication {

  val authenticationHeader: HttpHeader

}
