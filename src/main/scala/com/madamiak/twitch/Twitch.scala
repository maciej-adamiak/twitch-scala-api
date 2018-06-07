package com.madamiak.twitch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.endpoint._
import com.madamiak.twitch.client.{ HttpClient, TwitchClient }

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor, Future }
import scala.language.implicitConversions

//TODO give the possibility to choose the flavour basic / oauth
class Twitch(implicit val system: ActorSystem = ActorSystem("twitch-scala-sdk-system")) {

  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
//  implicit val client: TwitchOAuthClient          = new TwitchOAuthClient()
  implicit val client: HttpClient = new TwitchClient()

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
