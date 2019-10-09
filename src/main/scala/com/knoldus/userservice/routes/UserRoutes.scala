package com.knoldus.userservice.routes

import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.knoldus.userservice.data.model.{JsonSupport, MasterUser}
import com.knoldus.userservice.data.persistence.MasterUserComponent

import scala.concurrent.ExecutionContext

class UserRoutes(repo:MasterUserComponent#MasterUserRepository)(implicit ex: ExecutionContext)
  extends JsonSupport {

  val routes : Route  = path("getAll") {
    get {
      complete(repo.all)
    }
  }

  val registerRoute : Route = path("registerUser") {
    post {
      entity(as[MasterUser]) { user =>
        val stored = repo.registerUser(user)
        onComplete(stored) {
          done => complete("User registered")
        }
      }
      complete(repo.registerUser)
    }
  }
}
