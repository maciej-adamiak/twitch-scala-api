package com.madamiak.twitch.model.api.video

object VideoPeriod extends Enumeration {

  type Period = VideoPeriod.Value
  
  val All: Period   = Value("all")
  val Day: Period   = Value("day")
  val Week: Period  = Value("week")
  val Month: Period = Value("month")

}
