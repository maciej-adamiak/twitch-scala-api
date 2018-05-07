package com.madamiak.twitch.client

import com.madamiak.twitch.model.api.JsonSupport

trait Endpoint extends JsonSupport {

  private[client] val client: TwitchClient

}
