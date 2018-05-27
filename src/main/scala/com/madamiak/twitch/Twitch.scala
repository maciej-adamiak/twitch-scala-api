package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.client.endpoint._

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor, Future }
import scala.language.implicitConversions

class Twitch(implicit val system: ActorSystem = ActorSystem("twitch-scala-sdk-system")) {

  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
  implicit val client: TwitchClient               = new TwitchClient()

  val clips: ClipsEndpoint     = new ClipsEndpoint()
  val games: GamesEndpoint     = new GamesEndpoint()
  val streams: StreamsEndpoint = new StreamsEndpoint()
  val users: UsersEndpoint     = new UsersEndpoint()
  val videos: VideosEndpoint   = new VideosEndpoint()

  def shutdown(): Future[Unit] = {
    implicit val ece: ExecutionContextExecutor = system.dispatcher
    system.terminate.map(_ => (): Unit)
  }

}

object Twitch {

  def apply() = new Twitch()

}
