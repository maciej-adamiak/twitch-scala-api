package com.madamiak.twitch.model.api.stream

/**
  * Metadata about the Hearthstone hero selected by the broadcaster/opponent
  *
  * @param heroType Type of Hearthstone hero
  * @param heroClass Class of the Hearthstone hero
  * @param name Name of the Hearthstone hero
  */
case class HearthstoneHero(heroType: String, heroClass: String, name: String)
