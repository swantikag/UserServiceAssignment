package com.knoldus.userservice.data.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val masterUserFormat = jsonFormat2(MasterUser)
  implicit val allMasterUserFormat = jsonFormat1(AllMasterUsers)
  implicit val loginUserFormat = jsonFormat3(LoginUser)
}
