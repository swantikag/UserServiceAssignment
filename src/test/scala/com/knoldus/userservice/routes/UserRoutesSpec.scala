package com.knoldus.userservice.routes


import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.userservice.repo.{TestH2DBImpl, UserRepositoryTest}
import akka.http.scaladsl.server.Directives
import com.knoldus.userservice.data.model.{MasterUser, UserDetails}

import scala.concurrent.Future

class UserRoutesSpec extends WordSpec with  Matchers with ScalaFutures with ScalatestRouteTest
  with MasterUserComponent with LoginUserComponent {

  val masterRepo = new MasterUserRepository
  val loginRepo = new LoginUserRepository

  val userRoutes = new UserRoutes(masterRepo, loginRepo)
  val routes = userRoutes.routes

  implicit val dispatcher = system.dispatcher

  "User Service" should {
    "get display all registered users" in {
      Get("/getUsers") ~> routes ~> check {
        responseAs[String] shouldEqual """[]"""
      }
    }
  }

  trait MasterUserTestRepo extends MasterUserRepository with TestH2DBImpl {
    override def all() : Future[Seq[MasterUser]] = Future.successful(Seq(MasterUser("9622145423","Demo","demo@gmail.com",42, false)))
  }
}
