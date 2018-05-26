name := "twitch-scala-sdk"
organization := "com.madamiak"

version := "0.1.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.github.cb372" %% "scalacache-core" % "0.23.0",
  "com.github.cb372" %% "scalacache-caffeine" % "0.23.0",
  "com.github.ben-manes.caffeine" % "caffeine" % "2.6.2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-http-core" % "10.1.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test
)
