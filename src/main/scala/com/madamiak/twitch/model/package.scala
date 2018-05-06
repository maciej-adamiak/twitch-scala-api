package com.madamiak.twitch

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

package object model extends SprayJsonSupport with DefaultJsonProtocol {
  
  implicit def twitchDataFormat[T :JsonFormat]: RootJsonFormat[TwitchData[T]] = jsonFormat2(TwitchData[T])

  implicit val gameFormat: RootJsonFormat[Game] = jsonFormat(Game, "id", "name", "box_art_url")

  implicit val paginationFormat: RootJsonFormat[Pagination] = jsonFormat1(Pagination)

}
