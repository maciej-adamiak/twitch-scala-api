package com.madamiak.twitch.client.authentication

import akka.http.scaladsl.model.HttpHeader

trait Authentication {

  require(config.hasPath("twitch.client.id"), "Twitch api client id not defined")

  private[client] val clientId = config.getString("twitch.client.id")

  def authenticationHeader(): HttpHeader

}
