package com.madamiak.twitch.model.api.stream

/**
  * Represents metadata information about active streams playing Overwatch or Hearthstone
  *
  * @param gameId ID of the game being played on the stream
  * @param userId User ID of the streamer (broadcaster)
  * @param hearthstoneData Object containing the Hearthstone metadata
  * @param overwatchData Object containing the Overwatch metadata
  */
case class TwitchStreamMetadata(
    gameId: String,
    userId: String,
    hearthstoneData: Option[HearthstoneMetadata] = None,
    overwatchData: Option[OverwatchMetadata] = None,
)
