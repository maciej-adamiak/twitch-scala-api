package com.madamiak.twitch

import akka.actor.Terminated
import org.scalatest.{ AsyncWordSpec, Matchers }

class TwitchSpec extends AsyncWordSpec with Matchers {

  "twitch sdk entry point" can {

    "be instantiated using default actor system" in {
      Twitch().system.name shouldBe "twitch-scala-sdk-system"
    }

    "be shutdown" in {
      val twitch = Twitch()
      twitch.shutdown()
      twitch.system.whenTerminated.map(_ shouldBe a[Terminated])
    }
  }
}
