package com.knoldus.userservice.routes


import java.security.KeyPair

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.knoldus.userservice.data.model.{JsonSupport, LoginRequest, LoginUser, MasterUser, UserDetails, UserToken}
import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent}
import java.util.{Calendar, Date}

import io.jsonwebtoken.{Jwt, Jwts, SignatureAlgorithm}
import javax.crypto.SecretKey
import io.jsonwebtoken

import scala.util.Success

class UserRoutes(masterUserRepo:MasterUserComponent#MasterUserRepository) extends JsonSupport {

  lazy val ONE_MINUTE_IN_MILLIS = 60000
  private def login = post {
    entity(as[LoginRequest]) { user =>
      onComplete(masterUserRepo.authenticateUser(user)) {
        case util.Success(res) if res.length == 1 => onComplete(masterUserRepo.isEmailVerified(user)) {
          case util.Success(res) if res.head => {
            val date = Calendar.getInstance()
            val time = date.getTimeInMillis()
            val expiration = new Date(time + (10 * ONE_MINUTE_IN_MILLIS))
            val token = Jwts.builder()
                .setSubject(user.uname)
                .signWith(SignatureAlgorithm.HS256, "Secret_Key")
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact()
//            masterUserRepo.storeToken(UserToken(user.uname, token))
            complete("Welcome," + user.uname)
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

//  private def updatePassword = post{
//
//  }

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
//  path("passwordChange"){
//    updatePassword
//  }
}
