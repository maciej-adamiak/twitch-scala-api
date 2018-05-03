package com.madamiak.twitch.client.header

import akka.http.scaladsl.model.headers.{ModeledCustomHeader, ModeledCustomHeaderCompanion}

import scala.util.Try

/**
  * Obligatory twitch Client-ID header
  *
  * @param id client id acquired from the Twitch configuration dashboard
  */
final class ClientIdHeader(id: String) extends ModeledCustomHeader[ClientIdHeader] {
  
  override def companion: ModeledCustomHeaderCompanion[ClientIdHeader] = ClientIdHeader
  override def value(): String = id
  override def renderInRequests(): Boolean = true
  override def renderInResponses(): Boolean = true

}

object ClientIdHeader extends ModeledCustomHeaderCompanion[ClientIdHeader] {

  override val name = "Client-ID"
  override def parse(id: String): Try[ClientIdHeader] = Try(new ClientIdHeader(id))

}
