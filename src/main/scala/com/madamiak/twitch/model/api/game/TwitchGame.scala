package com.madamiak.twitch.model.api.game

import java.net.URL

/**
  * Represents Twitch game data
  *
  * @param id game id
  * @param name game name
  * @param boxArtUrl template URL for the game’s box art.
  */
case class TwitchGame(id: String, name: String, boxArtUrl: URL)
