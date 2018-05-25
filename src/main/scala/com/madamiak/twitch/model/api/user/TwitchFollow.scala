package com.madamiak.twitch.model.api.user

import java.util.Date

/**
  * Represents the relationships between two Twitch users
  *
  * @param fromId ID of the user following the to_id user
  * @param toId ID of the user being followed by the from_id user
  * @param followedAt Date and time when the from_id user followed the to_id user
  */
case class TwitchFollow(fromId: String, toId: String, followedAt: Date)
