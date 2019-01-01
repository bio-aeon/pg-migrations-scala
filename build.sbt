import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "su.wps",
      scalaVersion := "2.12.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "pg-migrations-scala",
    libraryDependencies ++= Seq(
      slf4jApi,
      log4Jdbc,
      specs2 % "test",
      testcontainersScala % "test",
      testcontainersPostgresql % "test",
      postgresql % "test")
  )
