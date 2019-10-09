package com.knoldus.userservice.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.knoldus.userservice.config.UserServiceConfig

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.Http

import scala.io.StdIn

trait HttpServer extends UserServiceConfig {
  implicit def system : ActorSystem
  implicit def materializer : Materializer
  implicit def ec : ExecutionContext
  def routes : Route
//  def routes1 : Route
  Http().bindAndHandle(routes, httpHost, httpPort)
//  println(s"Server online t http://localhost:8080/\nPress RETURN to stop...")
//  StdIn.readLine()
//  bindingFuture
//    .flatMap(_.unbind())
//    .onComplete(_ => system.terminate())
}
