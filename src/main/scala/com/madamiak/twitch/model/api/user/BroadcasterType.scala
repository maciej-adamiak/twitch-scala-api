package com.madamiak.twitch.model.api.user

/**
  * User’s broadcaster type
  */
object BroadcasterType extends Enumeration {

  type BroadcasterType = BroadcasterType.Value

  val Partner: BroadcasterType   = Value("partner")
  val Affiliate: BroadcasterType = Value("affiliate")
  val Undefined: BroadcasterType = Value("")

}
