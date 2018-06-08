package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.{HttpClient, TwitchClient}

import scala.concurrent.ExecutionContext

trait Auth {

  implicit val system: ActorSystem
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
  implicit val client: HttpClient = new TwitchClient()
  
}
