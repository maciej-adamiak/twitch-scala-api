package com.madamiak.twitch.model.api.stream

/**
  * Object containing the Hearthstone metadata
  *
  * @param broadcaster Hearthstone metadata about the broadcaster
  * @param opponent Hearthstone metadata about the broadcaster's opponent
  */
case class HearthstoneMetadata(broadcaster: HearthstonePlayer, opponent: HearthstonePlayer)
