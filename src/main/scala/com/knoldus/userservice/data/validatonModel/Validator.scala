package com.knoldus.userservice.data.validatonModel

trait Validator[T] extends (T => Seq[FieldErrorInfo]) {

  protected def validationStage(rule:Boolean, fieldName: String, errorText: String) : Option[FieldErrorInfo] =
    if (rule) Some(FieldErrorInfo(fieldName, errorText)) else None
}
