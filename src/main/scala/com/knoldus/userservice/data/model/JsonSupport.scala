package com.knoldus.userservice.data.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userDetailsFormat = jsonFormat6(UserDetails)
  implicit val masterTableFormat = jsonFormat5(MasterUser)
  implicit val loginUserFormat = jsonFormat3(LoginUser)
  implicit val authenticateUserFormat = jsonFormat2(LoginRequest)
}
