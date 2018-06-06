package com.madamiak.twitch.client

class TwitchAPIException(
    private val message: String = "",
    private val cause: Throwable = None.orNull
) extends RuntimeException(message, cause)
