package com.knoldus.userservice.service

import com.knoldus.userservice.model.{LoginRequest, LoginUser, MasterUser, UserDetails}

import scala.concurrent.Future

trait UserService {
  def validateEmail(email: String): Boolean
  def verifyEmail(email: String): Future[Int]
  def all: Future[Seq[MasterUser]]
  def registerUser(user: UserDetails): Future[(Int, Int)]
  def authenticateUser(user: LoginRequest): Future[Seq[LoginUser]]
  def isEmailVerified(user: LoginRequest): Future[Seq[Boolean]]
}
