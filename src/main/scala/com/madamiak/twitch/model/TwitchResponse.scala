package com.madamiak.twitch.model

case class TwitchResponse[T](rateLimit: RateLimit, twitchData: TwitchData[T])
