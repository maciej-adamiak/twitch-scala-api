package com.madamiak.twitch.model.api.authentication

/**
  * OAuth data
  *
  * @param accessToken oauth access token
  * @param expiresIn access token expiration time in seconds
  * @param scopes scopes relates to the access token
  */
case class AuthenticationData(accessToken: String, expiresIn: Long, scopes: Option[Seq[String]])
