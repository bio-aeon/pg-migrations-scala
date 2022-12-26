import sbt._

object Dependencies {
  lazy val slf4jApi = "org.slf4j" % "slf4j-api" % "2.0.6"
  lazy val specs2 = "org.specs2" %% "specs2-core" % "4.19.0"
  lazy val testcontainersScala = "com.dimafeng" %% "testcontainers-scala" % "0.40.12"
  lazy val testcontainersPostgresql = "org.testcontainers" % "postgresql" % "1.17.6"
  lazy val postgresql = "org.postgresql" % "postgresql" % "42.5.1"
}
