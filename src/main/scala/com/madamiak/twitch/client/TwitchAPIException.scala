package com.madamiak.twitch.client

//TODO status code
class TwitchAPIException(
    private val message: String = "",
    private val cause: Throwable = None.orNull
) extends RuntimeException(message, cause)
