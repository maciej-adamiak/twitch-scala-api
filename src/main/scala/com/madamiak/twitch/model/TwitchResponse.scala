package com.madamiak.twitch.model

import com.madamiak.twitch.model.api.TwitchPayload

/**
  * Represent a response of a single Twitch API call
  *
  * @param rateLimit Describes status of rate limits applied on the utilized endpoint
  * @param twitchPayload Contains response body
  * @tparam T Describes the type the body
  */
case class TwitchResponse[T](rateLimit: RateLimit, twitchPayload: TwitchPayload[T])
