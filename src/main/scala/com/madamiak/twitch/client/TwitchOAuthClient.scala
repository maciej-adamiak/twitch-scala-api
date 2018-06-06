package com.madamiak.twitch.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.authentication.AccessTokenAuthentication

import scala.concurrent.ExecutionContext

class TwitchOAuthClient(
    override implicit val system: ActorSystem,
    override implicit val context: ExecutionContext,
    override implicit val materializer: ActorMaterializer,
) extends HttpClient
    with AccessTokenAuthentication
