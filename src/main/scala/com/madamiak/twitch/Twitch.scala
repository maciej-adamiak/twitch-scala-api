package com.madamiak.twitch

import akka.actor.ActorSystem
import com.madamiak.twitch.client.endpoint._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.implicitConversions

class Twitch(implicit val system: ActorSystem = ActorSystem("twitch-scala-sdk-system")) {

  this: Auth =>

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

  def apply() = new Twitch() with Auth

}