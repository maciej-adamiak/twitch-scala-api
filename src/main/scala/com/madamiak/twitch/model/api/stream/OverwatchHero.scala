package com.madamiak.twitch.model.api.stream

/**
  * Overwatch metadata about the broadcaster
  *
  * @param ability ability being used by the broadcaster
  * @param role role of the Overwatch hero
  * @param name name of the Overwatch hero
  */
case class OverwatchHero(ability: String, role: String, name: String)
