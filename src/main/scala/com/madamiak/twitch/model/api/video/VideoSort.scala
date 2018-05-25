package com.madamiak.twitch.model.api.video

object VideoSort extends Enumeration {

  type Sort = VideoSort.Value
  
  val Time: Sort     = Value("time")
  val Trending: Sort = Value("trending")
  val Views: Sort    = Value("views")

}
