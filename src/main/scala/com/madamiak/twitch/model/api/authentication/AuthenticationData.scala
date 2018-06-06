package com.madamiak.twitch.model.api.authentication

/**
  * OAuth data
  *
  * @param accessToken oauth access token
  * @param expiresIn access token expiration time in seconds
  */
case class AuthenticationData(accessToken: String, expiresIn: Long)
