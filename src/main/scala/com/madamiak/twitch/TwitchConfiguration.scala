package com.madamiak.twitch

import com.typesafe.config.{ Config, ConfigFactory }

object TwitchConfiguration {

  val config: Config = ConfigFactory.load()

}
