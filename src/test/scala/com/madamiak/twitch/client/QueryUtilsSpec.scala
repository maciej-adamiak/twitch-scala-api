package com.madamiak.twitch.client

import org.scalatest.{Matchers, WordSpec}

class QueryUtilsSpec extends WordSpec with Matchers {

  "utils" should {

    "compute query" when {

      //TODO more descriptive tests i.e divide into separate
      "using miscellaneous parameters" in {

        val query = QueryUtils.query(
          "a1" -> 1,
          "a2" -> 1.0,
          "a3" -> "test",
          "a4" -> true,
          "a5" -> null,
          "b1" -> Seq(),
          "b2" -> Seq(1, 2, 3),
          "b3" -> Seq("a", "b", "c"),
          "b4" -> Seq(1.0, 2.0, 3.0),
          "b5" -> Seq(true, false),
          "c1" -> None,
          "c2" -> Some("a"),
          "c3" -> Some(2.0),
          "c4" -> Some(30),
          "c5" -> Some(Set("1")),
          "d1" -> Set("1"),
          "d2" -> Set(1, 2, 3),
          "d3" -> Set(),
          "e1" -> Map("a" -> 2)
        )

        val expected = Seq(
          ("a1", "1"),
          ("a2", "1.0"),
          ("a3", "test"),
          ("b2", "1"),
          ("b2", "2"),
          ("b2", "3"),
          ("b3", "a"),
          ("b3", "b"),
          ("b3", "c"),
          ("b4", "1.0"),
          ("b4", "2.0"),
          ("b4", "3.0"),
          ("b5", "true"),
          ("b5", "false"),
          ("c2", "a"),
          ("c3", "2.0"),
          ("c4", "30"),
          ("d1", "1"),
          ("d2", "1"),
          ("d2", "2"),
          ("d2", "3")
        )

        query.toList should contain theSameElementsAs expected

      }

    }
  }

}
