package com.madamiak.twitch.model.api.video

object Period extends Enumeration {

  type Period = Period.Value
  
  val All: Period   = Value("all")
  val Day: Period   = Value("day")
  val Week: Period  = Value("week")
  val Month: Period = Value("month")

}
