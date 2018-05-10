package com.madamiak.twitch.model.api.stream

/**
  * Metadata about the Hearthstone hero selected by the broadcaster/opponent
  *
  * @param heroType type of Hearthstone hero
  * @param heroClass class of the Hearthstone hero
  * @param name name of the Hearthstone hero
  */
case class HearthstoneHero(heroType: String, heroClass: String, name: String)
