package com.madamiak.twitch.model

/**
  * Represents Twitch default data wrapper
  * 
  * @param data wrapped data
  * @tparam T data type
  */
case class TwitchData[T](data: Seq[T], pagination: Pagination)