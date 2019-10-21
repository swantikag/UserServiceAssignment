package com.knoldus.userservice.routes


import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.knoldus.userservice.data.model.{JsonSupport, LoginRequest, LoginUser, MasterUser, UserDetails}
import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent}
import pdi.jwt.JwtAlgorithm.HS256
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader}
import java.time.Clock

import akka.http.javadsl.model.headers.RawHeader
import akka.http.scaladsl.model.{StatusCodes, headers}
import akka.http.scaladsl.model.headers.RawHeader

import scala.util.Success

class UserRoutes(masterUserRepo:MasterUserComponent#MasterUserRepository) extends JsonSupport {

  implicit val clock: Clock = Clock.systemUTC

  private def login = post {
    entity(as[LoginRequest]) { user =>
      onComplete(masterUserRepo.authenticateUser(user)) {
        case util.Success(res) if res.length == 1 => onComplete(masterUserRepo.isEmailVerified(user)) {
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
      val result = masterUserRepo.validateEmail(user.email)
      if (result) {
        onComplete(masterUserRepo.registerUser(user)){
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
      onComplete(masterUserRepo.verifyEmail(email)){
        case util.Success(1) => complete("Email ID verified")
        case util.Success(0) => complete("No user is registered with the given Email ID")
        case util.Failure(ex) => complete(ex.getMessage)
      }
    }
  }

  private def getAllUsers = get{
    complete(masterUserRepo.all)
  }

  val routes: Route = path("getUsers"){
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
