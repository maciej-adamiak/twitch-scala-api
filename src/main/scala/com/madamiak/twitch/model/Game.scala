package com.madamiak.twitch.model

/**
  * Represents Twitch game data
  *
  * @param id game id
  * @param name game name
  * @param boxArtUrl template URL for the game’s box art.
  */
case class Game(id: String, name: String, boxArtUrl: String)
