package com.madamiak.twitch.client

import akka.http.scaladsl.model.Uri.Query
import org.scalatest.{ Matchers, WordSpec }

class packageSpec extends WordSpec with Matchers {

  "converters" which {

    "calculate a query from maps" should {

      "prepare a valid query" when {

        "working with an empty sequence map" in {
          Map("a" -> Seq()).query shouldEqual Query()
        }

        "working with a singleton sequence map" in {
          Map("a" -> Seq(1)).query shouldEqual Query("a=1")
        }

        "working with a sequence map" in {
          Map("a" -> Seq(1), "b" -> Seq(1, 2, 3)).query shouldEqual Query("a=1&b=1&b=2&b=3")
        }

        "remove duplicates when working with a sequence map" in {
          Map("a" -> Seq(1), "b" -> Seq(1, 1, 1)).query shouldEqual Query("a=1&b=1")
        }

        "working with an empty option map" in {
          Map("a" -> None).query shouldEqual Query()
        }

        "working with a singleton option map" in {
          Map("a" -> Some(1)).query shouldEqual Query("a=1")
        }

        "working with a option map" in {
          Map("a" -> Some(1), "b" -> Some(2)).query shouldEqual Query("a=1&b=2")
        }

      }

    }

    "merge queries" should {

      "prepare a valid query" when {

        "working with two maps" in {
          Query("a=1&b=2") merge Query("c=3&d=4") shouldEqual Query("a=1&b=2&c=3&d=4")
        }
      }

      "remove duplicates" when {

        "working with two maps" in {
          Query("a=1&b=2") merge Query("a=1&b=2") shouldEqual Query("a=1&b=2")
        }

      }

    }

  }

}
