package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.model.api.JsonSupport

import scala.concurrent.ExecutionContext

trait Endpoint extends JsonSupport {

  implicit val system: ActorSystem
  implicit val context: ExecutionContext
  implicit val materializer: ActorMaterializer

  private[client] val client = new TwitchClient()

}
