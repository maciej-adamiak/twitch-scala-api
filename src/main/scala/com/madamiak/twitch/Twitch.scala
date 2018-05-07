package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.{ClipsEndpoint, GamesEndpoint, TwitchClient}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class Twitch(implicit val system: ActorSystem = ActorSystem("twitch-scala-client-system")) {

  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
  implicit val client: TwitchClient = new TwitchClient()

  val games: GamesEndpoint = new GamesEndpoint()
  val clips: ClipsEndpoint = new ClipsEndpoint()

  def shutdown(): Future[Unit] = {
    implicit val ece: ExecutionContextExecutor = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }

}
