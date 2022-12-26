import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "su.wps",
      scalaVersion := "2.13.10",
      version      := "0.1.1-SNAPSHOT"
    )),
    name := "pg-migrations-scala",
    libraryDependencies ++= Seq(
      slf4jApi,
      specs2 % "test",
      testcontainersScala % "test",
      testcontainersPostgresql % "test",
      postgresql % "test")
  )
