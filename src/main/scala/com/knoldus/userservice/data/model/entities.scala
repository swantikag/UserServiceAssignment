package com.knoldus.userservice.data.model

/**
 * Holds user details during registration
 * @param mobNumber is the unique mobille number for registering users
 * @param name is the name of the registering user
 * @param email is the email ID of the registering user
 * @param age is the age of the registering user
 * @param uname is the username of the registering user
 * @param password is the password of the registering user
 */
case class UserDetails(mobNumber:String,
                       name:String,
                       email:String,
                       age:Int,
                       uname:String,
                       password:String
                      )

/**
 * Holds details of the user details registered with the service
 * @param mobNumber is the unique mobille number for registered users
 * @param email is the email ID of the registered user
 * @param name is the name of the registered user
 * @param age is the age of the registered user
 * @param isVerified is the current status of the email ID. True if verified, else False
 */

case class MasterUser(mobNumber:String,
                      email:String,
                      name:String,
                      age:Int,
                      isVerified:Boolean
                     )

/**
 * Holds login information of the user
 * @param mobNumber is the unique mobile number
 * @param uname is the username of the user
 * @param password is the password of the user
 */
case class LoginUser(mobNumber:String,
                     uname:String,
                     password:String)

/**
 * Holds the information passed by the user during login request
 * @param uname is the entered username
 * @param password is the entered password
 */
case class LoginRequest(uname:String,
                            password:String)

