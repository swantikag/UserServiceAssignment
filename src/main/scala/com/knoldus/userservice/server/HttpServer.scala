package com.knoldus.userservice.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.knoldus.userservice.config.{DB, UserServiceConfig}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import akka.http.scaladsl.Http
import com.knoldus.userservice.routes.UserRoutesImpl

import scala.io.StdIn

class HttpServer(implicit val system : ActorSystem, implicit val materializer : Materializer)
  extends UserRoutesImpl with UserServiceConfig {
  def startServer(address: String, port: Int): Unit = {
    implicit val executor: ExecutionContext = system.dispatcher
    val bindingFuture = Http().bindAndHandle(userRoutes, httpHost, httpPort)
    println(s"Server online at http://localhost:8088/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

object HttpServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val server = new HttpServer()
    server.startServer("localhost", 8088)
  }
}
