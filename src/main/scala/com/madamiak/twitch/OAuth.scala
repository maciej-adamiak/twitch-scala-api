package com.madamiak.twitch

import com.madamiak.twitch.client.{HttpClient, TwitchOAuthClient}

trait OAuth extends Auth{

  override implicit val client: HttpClient = new TwitchOAuthClient()
  
}
