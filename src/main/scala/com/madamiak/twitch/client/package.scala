package com.madamiak.twitch

import akka.http.scaladsl.model.Uri.Query

package object client {

  implicit class RichSeq[T](val seq: Seq[T]) extends AnyVal {

    /**
      * Create a query string for a given sequence e.g. list of ids (1,2,3,4) to (id, 1), (id, 2) etc
      *
      * @param key common key used to form the query string
      * @return akka http query
      */
    def toQuery(key: String): Query = Query(seq.map(x => (key, x.toString)): _*)

  }

}
