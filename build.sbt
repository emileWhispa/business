name := "play-java-ebean-example"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.13.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.199"
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"


libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

libraryDependencies ++= Seq(
  javaWs
)

libraryDependencies += ehcache

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

// To provide an implementation of JAXB-API, which is required by Ebean.
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.3.1"
libraryDependencies += "javax.activation" % "activation" % "1.1.1"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"
