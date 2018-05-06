package com.madamiak.twitch.model.api

/**
  * Represents Twitch default data wrapper
  *
  * @param data wrapped data
  * @param pagination a cursor value
  * @tparam T data type
  */
case class TwitchData[T](data: Seq[T], pagination: Option[Pagination])
