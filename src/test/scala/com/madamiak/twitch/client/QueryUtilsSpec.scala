package com.madamiak.twitch.client

import org.scalatest.{ Matchers, WordSpec }

class QueryUtilsSpec extends WordSpec with Matchers {

  "query utils" should {

    "compute query" when {

      "using literal values" in {
        val query = QueryUtils.query(
          "a1" -> 1,
          "a2" -> 1.0,
          "a3" -> "test",
          "a4" -> true,
          "a5" -> null,
          "a6" -> 70L
        )
        query.toString shouldEqual "a1=1&a2=1.0&a3=test&a6=70"
      }

      "using sequenced values" in {
        val query = QueryUtils.query(
          "b1" -> Seq(),
          "b2" -> Seq(1, 2, 3),
          "b3" -> Seq("a", "b", "c"),
          "b4" -> Seq(1.0, 2.0, 3.0),
          "b5" -> Seq(true, false),
          "b6" -> Seq(1L),
          "b7" -> Seq("repeated", "repeated", "repeated", "repeated")
        )
        query.toString shouldEqual "b2=1&b2=2&b2=3&b3=a&b3=b&b3=c&b4=1.0&b4=2.0&b4=3.0&b5=true&b5=false&b6=1&b7=repeated"
      }

      "using sets" in {
        val query = QueryUtils.query(
          "d1" -> Set("1"),
          "d2" -> Set(1, 2, 3),
          "d3" -> Set()
        )
        query.toString shouldEqual "d1=1&d2=1&d2=2&d2=3"
      }

      "using optional values" in {
        val query = QueryUtils.query(
          "c1" -> None,
          "c2" -> Some("a"),
          "c3" -> Some(2.0),
          "c4" -> Some(30),
          "c5" -> Some(Set("1"))
        )
        query.toString shouldEqual "c2=a&c3=2.0&c4=30"
      }

      "using values obfuscated by illegal types" in {
        val query = QueryUtils.query(
          "a1" -> 1,
          "c5" -> Some(Set("1")),
          "e1" -> Map("a" -> 2),
          "f1" -> ("1", "1")
        )
        query.toString shouldEqual "a1=1"
      }

      "using mixed type values" in {
        val query = QueryUtils.query(
          "a1" -> 1,
          "b2" -> Seq(1, 2, 3),
          "c2" -> Some("a"),
          "d1" -> Set("1")
        )
        query.toString shouldEqual "a1=1&b2=1&b2=2&b2=3&c2=a&d1=1"
      }
    }
  }
}
