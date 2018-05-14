package com.madamiak.twitch.model.api.stream

/**
  * Represents an active Twitch stream
  *
  * @param communityIds Sequence of communities related to this stream
  * @param gameId Id of the game being played on the stream
  * @param id Stream id
  * @param language Language of the stream
  * @param startedAt UTC timestamp
  * @param thumbnailUrl Thumbnail URL of the stream
  * @param title Stream title
  * @param streamType Stream type: "live", "vodcast", or ""
  * @param userId ID of the user who is streaming
  * @param viewerCount Number of viewers watching the stream at the time of the quer
  */
case class TwitchStream(
    communityIds: Seq[String],
    gameId: String,
    id: String,
    language: String,
    startedAt: String,
    thumbnailUrl: String,
    title: String,
    //TODO to enum
    streamType: String,
    userId: String,
    viewerCount: Long
)
