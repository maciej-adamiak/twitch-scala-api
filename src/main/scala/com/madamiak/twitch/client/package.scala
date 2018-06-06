package com.madamiak.twitch

import com.typesafe.config.{ Config, ConfigFactory }

package object client {

  private[client] val config: Config = ConfigFactory.load()

}
