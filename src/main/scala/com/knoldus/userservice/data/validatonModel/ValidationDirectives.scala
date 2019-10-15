package com.knoldus.userservice.data.validatonModel

import akka.http.scaladsl.server.Rejection

final case class FieldErrorInfo(name: String, error: String)
final case class ModelValidationRejection(invalidFields:Seq[FieldErrorInfo]) extends Rejection
