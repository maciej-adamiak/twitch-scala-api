package com.madamiak.twitch.model.api.user

import java.net.URL

import com.madamiak.twitch.model.api.user.BroadcasterType.BroadcasterType
import com.madamiak.twitch.model.api.user.UserType.UserType

/**
  * Represents Twitch user data
  *
  * @param broadcasterType User’s broadcaster type
  * @param description User’s channel description
  * @param displayName User’s display name
  * @param email User’s email address
  * @param id User’s ID
  * @param login User’s login name
  * @param offlineImageUrl URL of the user’s offline image
  * @param profileImageUrl URL of the user’s profile image
  * @param userType User’s type
  * @param viewCount Total number of views of the user’s channel
  */
case class TwitchUser(
    broadcasterType: BroadcasterType,
    description: String,
    displayName: String,
    email: String,
    id: String,
    login: String,
    offlineImageUrl: URL,
    profileImageUrl: URL,
    userType: UserType,
    viewCount: Int
)
