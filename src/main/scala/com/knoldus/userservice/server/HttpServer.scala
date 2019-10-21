package com.knoldus.userservice.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.knoldus.userservice.config.UserServiceConfig

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.Http
import com.knoldus.userservice.data.persistence.DB

import scala.io.StdIn

trait HttpServer extends UserServiceConfig {
  implicit def system : ActorSystem
  implicit def materializer : Materializer
  implicit def ec : ExecutionContext
  val routes : Route
  val bindingFuture = Http().bindAndHandle(routes, httpHost, httpPort)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
