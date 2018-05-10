# Twitch Scala SDK

Twitch SDK for building application upon the [newest API](https://dev.twitch.tv/docs/api/#introduction)

## Usage

```scala
val twitch: Twitch = new Twitch()
val games: Future[TwitchResponse[TwitchGame]] = twitch.games.getGamesByName(Seq("PLAYERUNKNOWN'S BATTLEGROUNDS"))
```

## Supported API endoints 

| Resource | Endpoint         | Method |
| ---      | ---              | ---    |
| clips    | /helix/clips     | GET    |
| games    | /helix/games     | GET    |
| games    | /helix/games/top | GET    |
| streams  | /helix/streams   | GET    |

## Plans
- Integrate with all available enpoints
- Prepare mixins of endpoints to create commonly used querries e.g. find the streams of most popular games
- Simplify endpoints
- Simplify Twitch class usage
- Update twitch model case classes to use relevant datatypes despite of [constantly using strings in the API reponses](https://dev.twitch.tv/docs/api/reference/#get-streams)
- Identify Twitch API endpoints that can server duplicates

Any ideas and improvements are welcomed:
)
