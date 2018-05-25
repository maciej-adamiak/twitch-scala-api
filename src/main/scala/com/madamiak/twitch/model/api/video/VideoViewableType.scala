package com.madamiak.twitch.model.api.video

object VideoViewableType extends Enumeration {

  type ViewableType = VideoViewableType.Value

  val Public: ViewableType  = Value("public")
  val Private: ViewableType = Value("private")

}
