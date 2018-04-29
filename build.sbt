name := "timerboard-net-backend-kafka"

version := "0.1"

scalaVersion := "2.12.6"

lazy val http4sVersion = "0.18.9"
lazy val logbackVersion = "1.2.3"


libraryDependencies ++= Seq(
  "com.monovore" %% "decline" % "0.4.0",
  "com.lightbend" %% "kafka-streams-scala" % "0.2.1",
  "org.apache.kafka" % "kafka-streams" % "1.1.0",
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion
)

