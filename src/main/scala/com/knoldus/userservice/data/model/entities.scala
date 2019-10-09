package com.knoldus.userservice.data.model

case class MasterUser(mobNumber:String,
                      email:String)

case class LoginUser(mobNumber:String,
                     uname:String,
                     password:String)

case class AllMasterUsers(user:MasterUser)