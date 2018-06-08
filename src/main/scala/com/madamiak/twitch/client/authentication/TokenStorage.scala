package com.madamiak.twitch.client.authentication

import java.util.concurrent.TimeUnit

import com.github.benmanes.caffeine.cache.{ Caffeine, Cache => CCache }
import scalacache.Entry
import scalacache.caffeine.CaffeineCache
import scalacache.memoization.memoizeF
import scalacache.modes.scalaFuture._

import scala.concurrent.{ ExecutionContext, Future }

/**
  * TokenStorage is responsible for managing OAuth access tokens.
  * It supports token memoization (in-memory cache), manual (by explicitly calling evict)
  * and automatic eviction (60 days defined by Twitch API)
  *
  * @param context application execution context
  * @see https://dev.twitch.tv/docs/authentication/#types-of-tokens
  */
class TokenStorage(implicit context: ExecutionContext) {

  private val underlyingCache: CCache[String, Entry[String]] = Caffeine
    .newBuilder()
    .recordStats()
    .expireAfterWrite(60, TimeUnit.DAYS)
    .build[String, Entry[String]]

  private implicit val oauthDataCache: CaffeineCache[String] = CaffeineCache(underlyingCache)

  def store(f: => Future[String]): Future[String] = memoizeF(None)(f)

  def evict(): Future[Any] = oauthDataCache.removeAll()
}
