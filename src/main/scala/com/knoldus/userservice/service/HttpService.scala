package com.knoldus.userservice.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.knoldus.userservice.data.persistence.{MasterUserComponent, LoginUserComponent}
import com.knoldus.userservice.routes.UserRoutes

import scala.concurrent.ExecutionContextExecutor


trait HttpService extends MasterUserComponent with LoginUserComponent {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  lazy val masterUserRepo = new MasterUserRepository
  lazy val loginUserRepo = new LoginUserRepository
  val routes: Route = new UserRoutes(masterUserRepo, loginUserRepo).routes
}
