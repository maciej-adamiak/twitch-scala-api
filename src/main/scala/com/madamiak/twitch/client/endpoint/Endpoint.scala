package com.madamiak.twitch.client.endpoint

import com.madamiak.twitch.client.HttpClient

import scala.concurrent.{ ExecutionContext, Future }

trait Endpoint {

  private[client] val context: ExecutionContext

  private[client] val client: HttpClient

  private[client] def ~>[T](x: => Future[T]): Future[T] =
    Future {
      x
    }(context).flatten

}
