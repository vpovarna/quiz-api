val scala213 = "2.13.8"

ThisBuild / scalaVersion := scala213
ThisBuild / name := "quiz-api"
ThisBuild / version := "1.0"
ThisBuild / organization := "org.example.quiz"

val catsVersion = "2.1.1"
val catsEffectVersion = "3.2.0"
val http4sVersion = "1.0.0-M21"
val circeVersion = "0.14.0-M5"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
)

scalacOptions ++= Seq(
  "-language:higherKinds"
)