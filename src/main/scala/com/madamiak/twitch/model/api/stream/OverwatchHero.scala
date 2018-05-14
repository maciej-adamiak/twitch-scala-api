package com.madamiak.twitch.model.api.stream

/**
  * Overwatch metadata about the broadcaster
  *
  * @param ability Ability being used by the broadcaster
  * @param role Role of the Overwatch hero
  * @param name Name of the Overwatch hero
  */
case class OverwatchHero(ability: String, role: String, name: String)
