package com.madamiak.twitch.model.api

/**
  * Represents Twitch default data wrapper
  *
  * @param data Wrapped data
  * @param pagination Cursor value
  * @tparam T Data type
  */
case class TwitchPayload[T](data: Seq[T], pagination: Option[Pagination] = None)
