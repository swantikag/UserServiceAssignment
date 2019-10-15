package com.knoldus.userservice.data.validatonModel

import akka.http.scaladsl.server.Directives._
import com.knoldus.userservice.data.model.JsonSupport

object ValidatorDirective extends JsonSupport {

  import akka.http.scaladsl.server.Rejection

  def validateModel[T](model:T)(implicit validator:Validator[T]): Object={
    validator(model) match {
      case Nil => provide(model)
      case errors : Seq[FieldErrorInfo] => reject(ModelValidationRejection(errors))
    }
  }
}
