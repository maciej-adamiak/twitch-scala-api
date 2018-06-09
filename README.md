[![Build Status](https://travis-ci.org/maciej-adamiak/twitch-scala-sdk.svg?branch=master)](https://travis-ci.org/maciej-adamiak/twitch-scala-sdk)
[![codecov](https://codecov.io/gh/maciej-adamiak/twitch-scala-sdk/branch/master/graph/badge.svg)](https://codecov.io/gh/maciej-adamiak/twitch-scala-sdk)
[![CodeFactor](https://www.codefactor.io/repository/github/maciej-adamiak/twitch-scala-sdk/badge)](https://www.codefactor.io/repository/github/maciej-adamiak/twitch-scala-sdk)

# Twitch Scala SDK

Twitch SDK for building application upon the [helix API](https://dev.twitch.tv/docs/api/#introduction).

## Authentication

Scala SDK supports two different authentication mechanism available in the Twitch helix API:
- Client id - use the application client id to integrate with the API. This is the default in case no secret has been passed to the SDK,
- Application access token - fetches and stores an authentication token. This is automatically used when there is a secret available in the SDK. 

For more information refer to: [Twitch apps and authentication guide](https://dev.twitch.tv/docs/authentication/#introduction).

## Properties

| Name                 | Description                                                                                    | Default        |
| ---                  | ---                                                                                            | ---            |
| twitch.api.scheme    | Twitch helix API scheme                                                                        | https          |
| twitch.api.host      | Twitch helix host                                                                              | api.twitch.tv  |
| twitch.client.id     | Twitch application client id. Resolved also from `TWITCH_CLIENT_ID` environmental variable     | -              |
| twitch.client.secret | Twitch application client id. Resolved also from `TWITCH_CLIENT_SECRET` environmental variable | -              |
| twitch.client.scopes | Twitch application scopes                                                                      | empty list, [] |
| twitch.id.scheme     | Twitch identity server scheme                                                                  | https          |
| twitch.id.host       | Host of the Twitch identity server                                                             | id.twitch.tv   |
| twitch.id.path       | Path of the endpoint used to acquire an access token                                           | /oauth2/token  |

## Usage

```scala
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global
import com.madamiak.twitch.Twitch

val twitch = Twitch()

twitch.games.byName("PLAYERUNKNOWN'S BATTLEGROUNDS").onComplete {
  case Success(response) => for (game <- response.twitchPayload.data) println(game)
  case Failure(t)        => println("An error has occurred: " + t.getMessage)
}

twitch.streams.by(gameIds = Seq("123"), size = Some(10)).onComplete {
  case Success(response) => for (stream <- response.twitchPayload.data) println(stream)
  case Failure(t)        => println("An error has occurred: " + t.getMessage)
}

twitch.streams.metadata.byCommunityId("848d95be-90b3-44a5-b143-6e373754c382").onComplete {
  case Success(response) => for (metadata <- response.twitchPayload.data) println(metadata)
  case Failure(t)        => println("An error has occurred: " + t.getMessage)
}
```

```
TwitchGame(493057,PLAYERUNKNOWN'S BATTLEGROUNDS,https://static-cdn.jtvnw.net/ttv-boxart/PLAYERUNKNOWN%27S%20BATTLEGROUNDS-{width}x{height}.jpg)
TwitchStream(List(),33214,28643839600,en,2018-05-10T14:20:43Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_ninja-{width}x{height}.jpg,Prime/Fortnite Loot is LIVE | Sub with !prime to get yours in-game!,live,19571641,124218)
TwitchStream(List(),497416,28645088240,en,2018-05-10T16:41:47Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_shroud-{width}x{height}.jpg,rdy to get owned | Follow https://twitter.com/shroud,live,37402112,41743)
TwitchStream(List(),33214,28643227776,fr,2018-05-10T13:01:48Z,https://static-cdn.jtvnw.net/previews-ttv/live_user_gotaga-{width}x{height}.jpg,[FR] GOTAGA ► #FortniteGrind #Chill,live,24147592,20416)
```

## Supported API endpoints 

| Resource | Endpoint                | Method |
| ---      | ---                     | ---    |
| clips    | /helix/clips            | GET    |
| games    | /helix/games            | GET    |
| games    | /helix/games/top        | GET    |
| streams  | /helix/streams          | GET    |
| streams  | /helix/streams/metadata | GET    |
| users    | /helix/users            | GET    |
| users    | /helix/users/follows    | GET    |
| videos   | /helix/videos           | GET    |


Any ideas and improvements are welcomed :)
