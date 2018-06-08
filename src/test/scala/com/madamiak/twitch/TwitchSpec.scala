package com.madamiak.twitch

import org.scalatest.{Matchers, WordSpec}

class TwitchSpec extends WordSpec with Matchers {

  "twitch sdk entry point" can {

    "be instantiated using default actor system" in {
      val twitch = new Twitch() with OAuth
      
      Twitch().system.name shouldBe "twitch-scala-sdk-system"
    }
  }
}
