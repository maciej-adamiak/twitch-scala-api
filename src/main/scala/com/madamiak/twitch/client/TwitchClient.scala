package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.authentication.ClientIdAuthentication

import scala.concurrent.ExecutionContext

class TwitchClient(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer,
) extends HttpClient
    with ClientIdAuthentication
