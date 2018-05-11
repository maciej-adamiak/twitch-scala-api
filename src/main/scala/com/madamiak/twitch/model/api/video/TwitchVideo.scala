package com.madamiak.twitch.model.api.video

//TODO ALL
import com.madamiak.twitch.model.api.video.VideoType.VideoType
import com.madamiak.twitch.model.api.video.ViewType.ViewType

case class TwitchVideo(id: String,
                  language: String,
                  publishedAt: String,
                  thumbnailUrl: String,
                  title: String,
                  url: String,
                  userId: String,
                  viewCount: Int,
                  viewType: ViewType,
                  videoType: VideoType,
                  createdAt: String,
                  description: String,
                  duration: String)
