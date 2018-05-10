package com.madamiak.twitch.model.api.stream

/**
  * Overwatch metadata about the broadcaster
  *
  * @param hero Overwatch metadata about the broadcaster hero
  */
case class OverwatchPlayer(hero: Option[OverwatchHero] = None)
