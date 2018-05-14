package com.madamiak.twitch.model.api.video

object ViewType extends Enumeration {

  type ViewType = ViewType.Value

  val Public: ViewType  = Value("public")
  val Private: ViewType = Value("private")

}
