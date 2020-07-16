name := "Scala Notes"

ThisBuild / organization := "me.jerrywang.scala"

ThisBuild / version := "1.0.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.3"

ThisBuild / scalacOptions ++= Seq("-deprecation")

lazy val progfun = project in file("progfun")
