package com.madamiak.twitch.client

import akka.http.scaladsl.model.Uri.Query

object QueryUtils {

  def query(queryParameters: (String, Any)*): Query = {

    object SetExtractor {
      def unapplySeq[T](s: Set[T]): Option[Seq[T]] = Some(s.toSeq)
    }

    val parameters = queryParameters.flatMap {
      case (key, value) =>
        value match {
          case Some(value: String)                   => Seq((key, value))
          case Some(value: Number)                   => Seq((key, value.toString))
          case seq @ Seq(_: String, _ @_*)           => seq.distinct.map(i => (key.toString, i.toString))
          case seq @ Seq(_: Number, _ @_*)           => seq.distinct.map(i => (key.toString, i.toString))
          case seq @ Seq(_: Boolean, _ @_*)          => seq.distinct.map(i => (key.toString, i.toString))
          case set @ SetExtractor(_: String, _ @_*)  => set.map(i => (key.toString, i.toString))
          case set @ SetExtractor(_: Number, _ @_*)  => set.map(i => (key.toString, i.toString))
          case set @ SetExtractor(_: Boolean, _ @_*) => set.map(i => (key.toString, i.toString))
          case value: String                         => Seq((key, value))
          case value: Number                         => Seq((key, value.toString))
          case _                                     => Seq()
        }
    }
    Query(parameters: _*)
  }

}
