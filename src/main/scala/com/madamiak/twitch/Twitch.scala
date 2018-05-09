package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.{ ClipsEndpoint, GamesEndpoint, StreamsEndpoint, TwitchClient }

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor, Future }

class Twitch(implicit val system: ActorSystem = ActorSystem("twitch-scala-client-system")) {

  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
  implicit val client: TwitchClient               = new TwitchClient()

  val clips: ClipsEndpoint     = new ClipsEndpoint()
  val games: GamesEndpoint     = new GamesEndpoint()
  val streams: StreamsEndpoint = new StreamsEndpoint()

  def shutdown(): Future[Unit] = {
    implicit val ece: ExecutionContextExecutor = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }

}
