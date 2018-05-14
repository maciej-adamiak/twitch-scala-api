package com.madamiak.twitch.model.api.video

object ViewableType extends Enumeration {

  type ViewableType = ViewableType.Value

  val Public: ViewableType  = Value("public")
  val Private: ViewableType = Value("private")

}
