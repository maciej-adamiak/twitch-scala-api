package com.madamiak.twitch.model.api.video

import com.madamiak.twitch.model.api.video.VideoType.VideoType
import com.madamiak.twitch.model.api.video.ViewableType.ViewableType

/**
  * Represents a Twitch video
  *
  * @param id           ID of the video
  * @param language     Language of the video
  * @param publishedAt  Date when the video was published
  * @param thumbnailUrl Template URL for the thumbnail of the video
  * @param title        Title of the video
  * @param url          Trl of the video
  * @param userId       Id of the user who owns the video
  * @param viewCount    Number of times the video has been viewed.
  * @param viewableType Indicates whether the video is publicly viewable
  * @param videoType    Type of video
  * @param createdAt    Date when the video was created
  * @param description  Description of the video
  * @param duration     Length of the video
  */
case class TwitchVideo(
    id: String,
    language: String,
    publishedAt: String,
    thumbnailUrl: String,
    title: String,
    url: String,
    userId: String,
    viewCount: Int,
    viewableType: ViewableType,
    videoType: VideoType,
    createdAt: String,
    description: String,
    duration: String
)
