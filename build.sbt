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
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10"
  )
}