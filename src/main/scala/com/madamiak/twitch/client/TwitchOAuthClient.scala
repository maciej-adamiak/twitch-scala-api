package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.authentication.ApplicationAccessTokenAuthentication

import scala.concurrent.ExecutionContext

class TwitchOAuthClient(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext,
    implicit val materializer: ActorMaterializer,
) extends HttpClient
    with ApplicationAccessTokenAuthentication
