name := "akka-learn-scala"

version := "1.0"

scalaVersion := s"2.13.12"

resolvers +="Akka library repository".at("https://repo.akka.io/maven")

lazy val akkaVersion = "2.9.0"

fork := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.12",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)