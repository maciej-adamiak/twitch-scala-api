package com.madamiak.twitch.client

import com.typesafe.config.{ Config, ConfigFactory }

package object authentication {

  private[authentication] val config: Config = ConfigFactory.load()

}
