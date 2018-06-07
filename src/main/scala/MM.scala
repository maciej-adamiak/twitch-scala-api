import com.madamiak.twitch.Twitch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

object MM extends App {

  val twitch = Twitch()

  twitch.games.byName("PLAYERUNKNOWN'S BATTLEGROUNDS").onComplete {
    case Success(response) => {
      for (game <- response.twitchPayload.data) println(game)
      twitch.shutdown()
    }
    case Failure(t) => {
      println("An error has occurred: " + t.getMessage)
      twitch.shutdown()
    }
  }

}
