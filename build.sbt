name := "UserManagementSystem"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in Compile := Some("UserService")

libraryDependencies ++= {
  val slickVersion           = "3.3.2"
  val mySqlVersion           = "8.0.17"
  Seq(
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "mysql" % "mysql-connector-java" % mySqlVersion,
    "com.typesafe.akka" %% "akka-http"   % "10.1.10",
    "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    "com.h2database" % "h2" % "1.4.192",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.10",
    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test
  )
}

coverageEnabled := true