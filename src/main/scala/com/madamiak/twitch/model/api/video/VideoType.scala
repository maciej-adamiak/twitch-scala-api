package com.madamiak.twitch.model.api.video

object VideoType extends Enumeration {

  type VideoType = VideoType.Value

  val All: VideoType       = Value("all")
  val Upload: VideoType    = Value("upload")
  val Archive: VideoType   = Value("archive")
  val Highlight: VideoType = Value("highlight")

}
