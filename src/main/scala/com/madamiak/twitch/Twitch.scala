package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.{ClipsEndpoint, GamesEndpoint}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

//TODO check conf on creation
class Twitch(
    implicit val system: ActorSystem = ActorSystem("twitch-scala-client-system"),
) {
  
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val games = new GamesEndpoint()
  val clips = new ClipsEndpoint()

  def shutdown(): Future[Unit] = {
    implicit val ece: ExecutionContextExecutor = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }

}