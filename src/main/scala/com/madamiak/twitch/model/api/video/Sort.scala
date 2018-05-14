package com.madamiak.twitch.model.api.video

object Sort extends Enumeration {

  type Sort = Sort.Value
  
  val Time: Sort     = Value("time")
  val Trending: Sort = Value("trending")
  val Views: Sort    = Value("views")

}
