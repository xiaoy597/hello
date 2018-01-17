import sbt.Keys.libraryDependencies

name := "hello"
version := "1.0"
scalaVersion := "2.11.12"

lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.12",
  libraryDependencies += "org.specs2" % "specs2_2.11" % "3.7" % "test"

)

val gitCommitSha = taskKey[String]("Determines the current git commit SHA")

gitCommitSha := Process("git rev-parse HEAD").lines.head

val makeVersionProperties = taskKey[Seq[File]]("Make a version.properties file")

makeVersionProperties := {
  val propFile = new File((resourceManaged in Compile).value, "version.properties")
  val content = "version=%s" format gitCommitSha.value
  IO.write(propFile, content)
  Seq(propFile)
}

resourceGenerators in Compile += makeVersionProperties

lazy val common = (project in file("common")).settings(commonSettings)
