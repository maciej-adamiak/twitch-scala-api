package com.madamiak.twitch.model.api

/**
  * Represent scrolling / pagination
  *
  * @param cursor Value, to be used in a subsequent request to specify the starting point of the next set of results.
  */
case class Pagination(cursor: String)
