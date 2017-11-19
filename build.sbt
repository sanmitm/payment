name := """play-scala-forms-example"""

version := "2.6.x"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % "test"
libraryDependencies += ws
libraryDependencies += "com.typesafe.play" %% "play-ws" % "2.5.4"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"
libraryDependencies += "net.liftweb" %% "lift-webkit" % "3.1.0"


libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1206-jdbc4"
libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions