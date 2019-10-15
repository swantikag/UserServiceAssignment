package com.knoldus.userservice.repo

import com.knoldus.userservice.data.model.{LoginRequest, LoginUser, MasterUser}
import com.knoldus.userservice.data.persistence.{LoginUserComponent, MasterUserComponent}
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class UserRepositoryTest extends FunSuite with MasterUserComponent with LoginUserComponent with TestH2DBImpl with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  val masterUserRepository = new MasterUserRepository
  val loginUserRepository = new LoginUserRepository

  test("Register new user") {
    val testUser = MasterUser("8492825775", "Demo", "demo@gmail.com", 42, false)
    val response = masterUserRepository.registerUser(testUser)
    whenReady(response){ status =>
      assert(status === 1)
    }
  }

  test("user with invalid email id should not be registered") {
    val response = masterUserRepository.verifyEmail("demo...@xyz")
    assert(response === false)
  }

  test("login existing user") {
    val response = loginUserRepository.authenticateUser(LoginRequest("James", "James123"))
    whenReady(response){ result =>
      assert(result === Vector(LoginUser("9622142113", "James", "James123")))
    }
  }

}
