package com.knoldus.userservice.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.knoldus.userservice.config.UserServiceConfig

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.Http

trait HttpServer extends UserServiceConfig {
  implicit def system : ActorSystem
  implicit def materializer : Materializer
  implicit def ec : ExecutionContext
  val routes : Route
  Http().bindAndHandle(routes, httpHost, httpPort)
}
