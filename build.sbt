import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "su.wps",
      scalaVersion := "2.12.5",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "pg-migrations-scala",
    libraryDependencies ++= Seq(
      specs2,
      slf4jApi,
      log4Jdbc,
      testcontainersScala % "test",
      testcontainersPostgresql % "test",
      postgresql % "test")
  )
