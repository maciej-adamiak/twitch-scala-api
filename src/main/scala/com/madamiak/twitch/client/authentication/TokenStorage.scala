package com.madamiak.twitch.client.authentication

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{ Cache, CachingSettings }

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

/**
  * TokenStorage is responsible for managing OAuth access tokens.
  * It supports token memoization (in-memory cache), manual (by explicitly calling evict)
  * and automatic eviction (60 days defined by Twitch API)
  *
  * @param context application execution context
  * @see https://dev.twitch.tv/docs/authentication/#types-of-tokens
  */
class TokenStorage(
    implicit val system: ActorSystem,
    implicit val context: ExecutionContext
) {

  private val defaultCachingSettings = CachingSettings(system)
  private val lfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withTimeToLive(100.days)
    .withMaxCapacity(1)
    .withInitialCapacity(1)
  private val cachingSettings                 = defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)
  private val lfuCache: Cache[String, String] = LfuCache(cachingSettings)

  def store(f: => Future[String]): Future[String] = lfuCache.getOrLoad("access_token", _ => f)

  def evict(): Unit = lfuCache.remove("access_token")

  def size(): Long = lfuCache.size()
}
