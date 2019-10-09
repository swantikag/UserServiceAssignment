package com.knoldus.userservice.service

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.knoldus.userservice.data.persistence.{DB, MasterUserComponent}
import com.knoldus.userservice.routes.UserRoutes


trait HttpService extends MasterUserComponent with DB {
  implicit lazy val system = ActorSystem()
  implicit lazy val materializer = ActorMaterializer()
  implicit lazy val ec = system.dispatcher

  lazy val userRepo = new MasterUserRepository
  lazy val routes = new UserRoutes(userRepo).routes
}
