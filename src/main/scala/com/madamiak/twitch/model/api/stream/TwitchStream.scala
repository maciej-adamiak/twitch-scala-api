package com.madamiak.twitch.model.api.stream

/**
  * Represents an active Twitch stream
  *
  * @param communityIds sequence of communities related to this stream
  * @param gameId id of the game being played on the stream
  * @param id stream id
  * @param language language of the stream
  * @param startedAt UTC timestamp
  * @param thumbnailUrl thumbnail URL of the stream
  * @param title stream title
  * @param streamType stream type: "live", "vodcast", or ""
  * @param userId ID of the user who is streaming
  * @param viewerCount number of viewers watching the stream at the time of the quer
  */
case class TwitchStream(communityIds: Seq[String],
                        gameId: String,
                        id: String,
                        language: String,
                        startedAt: String,
                        thumbnailUrl: String,
                        title: String,
                        streamType: String,
                        userId: String,
                        viewerCount: Long)
