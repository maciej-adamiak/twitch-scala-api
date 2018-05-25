# Twitch Scala SDK

Twitch SDK for building application upon the [newest API](https://dev.twitch.tv/docs/api/#introduction)

## Usage

```scala
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global
import com.madamiak.twitch.Twitch

val twitch = Twitch()

twitch.games.getByName(Seq("PLAYERUNKNOWN'S BATTLEGROUNDS")).onComplete {
  case Success(response) => for (game <- response.twitchPayload.data) println(game)
  case Failure(t)        => println("An error has occurred: " + t.getMessage)
}

twitch.streams.get(first = Some(10)).onComplete {
  case Success(response) => for (stream <- response.twitchPayload.data) println(stream)
  case Failure(t)        => println("An error has occurred: " + t.getMessage)
}
```

```
TwitchGame(493057,PLAYERUNKNOWN'S BATTLEGROUNDS,https://static-cdn.jtvnw.net/ttv-boxart/PLAYERUNKNOWN%27S%20BATTLEGROUNDS-{width}x{height}.jpg)
TwitchStream(List(),33214,28643839600,en,2018-05-10T14:20:43Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_ninja-{width}x{height}.jpg,Prime/Fortnite Loot is LIVE | Sub with !prime to get yours in-game!,live,19571641,124218)
TwitchStream(List(),497416,28645088240,en,2018-05-10T16:41:47Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_shroud-{width}x{height}.jpg,rdy to get owned | Follow https://twitter.com/shroud,live,37402112,41743)
TwitchStream(List(),33214,28643227776,fr,2018-05-10T13:01:48Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_gotaga-{width}x{height}.jpg,[FR] GOTAGA ► #FortniteGrind #Chill,live,24147592,20416)
```

## Supported API endoints 

| Resource | Endpoint                | Method |
| ---      | ---                     | ---    |
| clips    | /helix/clips            | GET    |
| games    | /helix/games            | GET    |
| games    | /helix/games/top        | GET    |
| streams  | /helix/streams          | GET    |
| streams  | /helix/streams/metadata | GET    |
| users    | /helix/users            | GET    |
| users    | /helix/users/follows    | GET    |
| videos   | /helix/streams/videos   | GET    |

## Plans
- Prepare mixins of endpoints to create commonly used queries e.g. find the streams of most popular games
- Use pagination like an iterable

Any ideas and improvements are welcomed:
)
