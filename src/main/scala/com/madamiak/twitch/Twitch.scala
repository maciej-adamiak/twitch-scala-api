package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.GamesEndpoint

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class Twitch(
    implicit val system: ActorSystem = ActorSystem("twitch-scala-client-system"),
) {
  
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  
  //TODO check conf on creation
  
  val games = new GamesEndpoint()

  def shutdown(): Future[Unit] = {
    implicit val ece: ExecutionContextExecutor = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }

}