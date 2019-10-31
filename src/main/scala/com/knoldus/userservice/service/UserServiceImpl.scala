package com.knoldus.userservice.service

import com.knoldus.userservice.Dao.{SQLImpl, UserDao, UserDaoImpl}
import com.knoldus.userservice.model.{LoginRequest, LoginUser, MasterUser, UserDetails}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UserServiceImpl extends UserServiceImpl {
  val userDao : UserDao = new UserDaoImpl with SQLImpl
}

trait UserServiceImpl extends UserService {

  val userDao : UserDao
  /**
   * Validates the entered email ID to accept only allowed email Id format
   * @param email is the entered email ID
   * @return true if the email ID is of appropriate format, else false
   */
  def validateEmail(email: String): Boolean = {
    import scala.util.matching.Regex
    val pattern = new Regex("[A-Za-z]{1,}[0-9]*.?[A-Za-z-0-9]*@[A-Za-z0-9.]+\\.[a-zA-Z]{2,3}$")
    (pattern.findFirstIn(email)) match {
      case Some(valid) => true
      case None => false
    }
  }

  def verifyEmail(email: String): Future[Int] = {
    userDao.verifyEmail(email)
  }

  def all: Future[Seq[MasterUser]] = {
    userDao.all
  }

  def registerUser(user: UserDetails): Future[(Int, Int)] = {
    userDao.registerUser(user)
  }

  def authenticateUser(user: LoginRequest): Future[Seq[LoginUser]] = {
    userDao.authenticateUser(user)
  }

  def isEmailVerified(user: LoginRequest): Future[Seq[Boolean]] = {
    userDao.isEmailVerified(user)
  }

}
