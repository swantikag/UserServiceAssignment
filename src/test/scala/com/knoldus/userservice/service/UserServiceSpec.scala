package com.knoldus.userservice.service

import com.knoldus.userservice.Dao.{UserDao, UserDaoImpl}
import com.knoldus.userservice.model.{LoginRequest, LoginUser, MasterUser, UserDetails}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceSpec extends FlatSpec with Matchers with MockitoSugar with ScalaFutures {

  val mockDao = mock[UserDaoImpl]

  object TestObject extends UserServiceImpl {
    val userDao: UserDao = mockDao
  }

  val testUserMaster = MasterUser("7546981235", "testing@gmail.com", "testing", 25, false)
  val testUserLogin = LoginUser("8532145658","test","test@31")

  val testUser = UserDetails("7546981235", "testing", "testing@gmail.com", 25, "test123", "test@123")

  "The service " should
    "return list of all registered users" in {
    when(mockDao.all).thenReturn(Future[Seq[MasterUser]](Seq(testUserMaster)))
    whenReady(TestObject.all) { result =>
      assert(result === List(testUserMaster))
    }
  }
  it should
    "register a user with valid email ID" in {
      when(mockDao.registerUser(testUser)).thenReturn(Future.successful((1, 1)))
      whenReady(TestObject.registerUser(testUser)) { result =>
        assert(result === (1, 1))
      }
    }

  it should
  "return false for a malformed email ID" in {
    assert(TestObject.validateEmail("..123@hh")===false)
  }

  it should
  "set the status of the email ID to verified" in {
    when(mockDao.verifyEmail("testing@gmail.com")).thenReturn(Future.successful(1))
    whenReady(TestObject.verifyEmail("testing@gmail.com")){result =>
      assert(result===1)
    }
  }

  it should
    "return verification status of email ID" in {
      when(mockDao.isEmailVerified(LoginRequest("test","test@31"))).thenReturn(Future[Seq[Boolean]](Seq(false)))
    whenReady(TestObject.isEmailVerified(LoginRequest("test","test@31"))){result =>
      assert(result === Vector(false))
    }
    }

  it should
  "authenticate credentials of logging in user" in{
    when(mockDao.authenticateUser(LoginRequest("test","test@31"))).thenReturn(Future[Seq[LoginUser]](Seq(testUserLogin)))
    whenReady(TestObject.authenticateUser(LoginRequest("test","test@31"))){result =>
      assert(result === Vector(testUserLogin))
    }
  }
}
