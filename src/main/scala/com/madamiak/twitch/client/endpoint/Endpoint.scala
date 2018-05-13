package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.TwitchClient
import com.madamiak.twitch.model.api.JsonSupport

import scala.concurrent.{ExecutionContext, Future}

trait Endpoint extends JsonSupport {

  private[client] val context: ExecutionContext
  
  private[client] val client: TwitchClient

  def ~>[T](x: => Future[T]): Future[T] = Future {
    x
  }(context).flatten
  
}
