package com.knoldus.userservice.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent, SQLImpl}
import com.knoldus.userservice.routes.UserRoutes

import scala.concurrent.ExecutionContextExecutor


trait HttpService extends MasterUserComponent {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  lazy val masterUserRepo = new MasterUserRepository with SQLImpl
  val routes: Route = new UserRoutes(masterUserRepo).routes
}
