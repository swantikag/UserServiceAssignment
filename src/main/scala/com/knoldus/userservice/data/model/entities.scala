package com.knoldus.userservice.data.model

import com.knoldus.userservice.data.validatonModel.Validator

case class UserDetails(mobNumber:String,
                       name:String,
                       email:String,
                       age:Int,
                       uname:String,
                       password:String
                      )

case class MasterUser(mobNumber:String,
                      email:String,
                      name:String,
                      age:Int,
                      isVerified:Boolean
                     )

case class LoginUser(mobNumber:String,
                     uname:String,
                     password:String)

case class LoginRequest(uname:String,
                            password:String)

//object UserDetailsValidator extends Validator[UserDetails] {
//  def emailRule(email:String): Boolean = if ("""[A-Za-z]{1,}[0-9]*.?[A-Za-z-0-9]*@[A-Za-z0-9.]+\\.[a-zA-Z]{2,3}""".r.findFirstIn(email)==None)
//    true else false
//  def ageRule(age:Option[Int]): Boolean =if(age.isDefined && (age.get < 18)) true else false
//}
