credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("SONATYPE_USER", ""),
  sys.env.getOrElse("SONATYPE_PASS", "")
)

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://maciej-adamiak.github.io/twitch-scala-sdk/"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/maciej-adamiak/twitch-scala-sdk"),
    "scm:git@github.com:maciej-adamiak/twitch-scala-sdk.git"
  )
)

developers := List(
  Developer(
    id    = "madamiak",
    name  = "Maciej Adamiak",
    email = "adamiak.maciek@gmail.com",
    url   = url("https://github.com/maciej-adamiak")
  )
)