credentials ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("http://example.com"))

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