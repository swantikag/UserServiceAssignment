package com.knoldus.userservice.routes


import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import java.time.Clock

import com.knoldus.userservice.model.{JsonSupport, LoginRequest, UserDetails}
import com.knoldus.userservice.service.{UserService, UserServiceImpl}
import pdi.jwt.{Jwt, JwtAlgorithm}

trait UserRoutes extends JsonSupport {

  val userService : UserService

  implicit val clock: Clock = Clock.systemUTC

  private def login = post {
    entity(as[LoginRequest]) { user =>
      onComplete(userService.authenticateUser(user)) {
        case util.Success(res) if res.length == 1 => onComplete(userService.isEmailVerified(user)) {
          case util.Success(res) if res.head => {
            val claims = Jwt.encode(user.uname,"secretKey", JwtAlgorithm.HS256)
            complete("Welcome," + user.uname + "\nToken: " + claims)
          }
          case util.Success(res) => complete("Email ID for the user is not verified")
          case util.Failure(ex) => complete(ex.getMessage)
        }
        case util.Success(res) if res.isEmpty => complete("Invalid credentials")
        case util.Failure(ex) => complete(ex.getMessage)
      }
      }
    }

  private def registerUser = post{
    entity(as[UserDetails]) { user =>
      val result = userService.validateEmail(user.email)
      if (result) {
        onComplete(userService.registerUser(user)){
          case util.Success(_) => complete("User Registered Successfully")
          case util.Failure(ex) => complete(ex.getMessage)
        }
      }
        else {
        complete("Invalid Email ID")
      }
    }
  }

  private def verifyEmail = get{
    parameter("email"){ email =>
      onComplete(userService.verifyEmail(email)){
        case util.Success(1) => complete("Email ID verified")
        case util.Success(0) => complete("No user is registered with the given Email ID")
        case util.Failure(ex) => complete(ex.getMessage)
      }
    }
  }

  private def getAllUsers = get{
    complete(userService.all)
  }

  val userRoutes: Route = path("getUsers"){
    getAllUsers
  } ~
  path("registerUser"){
    registerUser
  } ~
  path("loginUser"){
    login
  } ~
  path("verifyEmail"){
    verifyEmail
  }
}

class UserRoutesImpl extends UserRoutes {
  val userService = UserServiceImpl
}
