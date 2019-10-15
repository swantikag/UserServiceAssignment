package com.knoldus.userservice.routes


import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.knoldus.userservice.data.model.{JsonSupport, LoginRequest, LoginUser, MasterUser, UserDetails}
import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent}

import scala.concurrent.ExecutionContext.Implicits
import scala.util.Try

class UserRoutes(masterUserRepo:MasterUserComponent#MasterUserRepository, loginUserRepo:LoginUserComponent#LoginUserRepository) extends JsonSupport {

  private def login = post{
    entity(as[LoginRequest]) { user =>
      onComplete(loginUserRepo.authenticateUser(user)){
        case util.Success(res) if res.head => complete("Welcome, " + user.uname)
        case util.Success(res)  => complete("User not verified")
        case util.Failure(ex) => complete(ex.getMessage)
      }
    }
  }

  private def registerUser = post{
    entity(as[UserDetails]) { user =>
      val result = masterUserRepo.validateEmail(user.email)
      if (result) {
        onComplete(masterUserRepo.registerUser(MasterUser(user.mobNumber, user.email, user.name, user.age, false))) {
          case util.Success(_) => onComplete(loginUserRepo.insertLoginTable(LoginUser(user.mobNumber, user.password, user.password))) {
            case util.Success(_) => complete("User Registered Successfully")
            case util.Failure(ex) => complete(ex.getMessage)
          }
          case util.Failure(ex) => complete(ex.getMessage)
        }
      } else {
        complete("Invalid Email ID")
      }
    }
  }
  private def verifyEmail = get{
    parameter("email"){ email =>
      onComplete(masterUserRepo.verifyEmail(email)){
        case util.Success(1) => complete("Email ID verified")
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
  }
  path("verifyEmail"){
    verifyEmail
  }
}
