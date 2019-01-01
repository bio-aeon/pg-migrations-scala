import sbt._

object Dependencies {
  lazy val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.25"
  lazy val log4Jdbc = "com.googlecode.log4jdbc" % "log4jdbc" % "1.2"
  lazy val specs2 = "org.specs2" %% "specs2-core" % "4.3.6"
  lazy val testcontainersScala = "com.dimafeng" %% "testcontainers-scala" % "0.22.0"
  lazy val testcontainersPostgresql = "org.testcontainers" % "postgresql" % "1.10.4"
  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"
}
