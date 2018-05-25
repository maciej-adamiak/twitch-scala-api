package com.madamiak.twitch.model.api.stream

object StreamType extends Enumeration {

  type StreamType = StreamType.Value

  val Live: StreamType           = Value("live")
  val Malfunctioning: StreamType = Value("")

}
