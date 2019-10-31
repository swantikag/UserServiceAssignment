package com.knoldus.userservice.routes

import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.userservice.repo.TestDBImpl
import akka.util.ByteString
import com.knoldus.userservice.model.{JsonSupport, LoginRequest, LoginUser, UserDetails}
import com.knoldus.userservice.service.{UserService, UserServiceImpl}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

class UserRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport with MockitoSugar {

  val mockRepo = mock[UserServiceImpl]

  object TestObject extends UserRoutes {
    val userService = mockRepo
  }

  val testUser = UserDetails("7546981235", "testing", "testing@gmail.com", 25, "test123", "test@123")

  "The service" should {

    "register a new user with valid email ID to the database" in {

      when(mockRepo.validateEmail("testing@gmail.com")).thenReturn(true)
      when(mockRepo.registerUser(testUser)).thenReturn(Future[(Int,Int)]((1,1)))

      val jsonData = ByteString(
        """{"mobNumber":"7546981235",
          |"email":"testing@gmail.com",
          |"name":"testing",
          |"age":25,
          |"uname":"test123",
          |"password":"test@123"}""".stripMargin)

      val postRequest = HttpRequest(HttpMethods.POST,
        uri = "/registerUser",
        entity = HttpEntity(MediaTypes.`application/json`, jsonData))

      postRequest ~> TestObject.userRoutes ~> check {
        status.isSuccess() shouldEqual (true)
        responseAs[String] shouldEqual ("User Registered Successfully")
      }
    }
  }

  "The service" should {
      "not allow a registered user with unverified email ID to log in" in {
        val loginRequest = ByteString("""{"uname":"test123", "password":"test@123"}""")
        when(mockRepo.authenticateUser(LoginRequest("test123", "test@123")))
          .thenReturn(Future[Seq[LoginUser]](Seq(LoginUser("7894561235", "test123", "test@123"))))
        when(mockRepo.isEmailVerified(LoginRequest("test123", "test@123")))
          .thenReturn(Future[Seq[Boolean]](Seq(false)))
        val postRequest = HttpRequest(HttpMethods.POST,
          uri = "/loginUser",
          entity = HttpEntity(MediaTypes.`application/json`, loginRequest))
        postRequest ~> TestObject.userRoutes ~> check {
          status.isSuccess() shouldEqual true
          responseAs[String] shouldEqual ("Email ID for the user is not verified")
        }
      }
    }
    {
      "not allow a login request with wrong credentials to log in" in {
        val loginRequest = ByteString("""{"uname":"dummy", "password":"dummy@123"}""")
        when(mockRepo.authenticateUser(LoginRequest("dummy", "dummy@123")))
          .thenReturn(Future[Seq[LoginUser]](Seq.empty))
        val postRequest = HttpRequest(HttpMethods.POST,
          uri = "/loginUser",
          entity = HttpEntity(MediaTypes.`application/json`, loginRequest))

        postRequest ~> TestObject.userRoutes ~> check {
          status.isSuccess() shouldEqual true
          responseAs[String] shouldEqual ("Invalid credentials")
        }
      }
    }

  "The service" should {
    "allow an registered email ID to be verified" in {
      when(mockRepo.verifyEmail("testing@gmail.com")).thenReturn(Future[Int](1))
      Get("/verifyEmail?email=testing@gmail.com") ~> TestObject.userRoutes ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual("Email ID verified")
      }
    }

    "does not allow an unregistered email ID to be verified" in {
      when(mockRepo.verifyEmail("demo@gmail.com")).thenReturn(Future[Int](0))
      Get("/verifyEmail?email=demo@gmail.com") ~> TestObject.userRoutes ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual("No user is registered with the given Email ID")
      }
    }

  }

  "The service" should {
    "allow a registered user with verified email to log in" in {
      val loginRequest = ByteString("""{"uname":"test123", "password":"test@123"}""")
      when(mockRepo.authenticateUser(LoginRequest("test123", "test@123")))
        .thenReturn(Future[Seq[LoginUser]](Seq(LoginUser("7894561235", "test123", "test@123"))))
      when(mockRepo.isEmailVerified(LoginRequest("test123", "test@123")))
        .thenReturn(Future[Seq[Boolean]](Seq(true)))
      val postRequest = HttpRequest(HttpMethods.POST,
        uri = "/loginUser",
        entity = HttpEntity(MediaTypes.`application/json`, loginRequest))
      postRequest ~> TestObject.userRoutes ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual ("Welcome,test123\nToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.dGVzdDEyMw.8m4JajSfRN6sMFlKSzzCJNQKQmEdZAyd-SAJxzIoMuc")
      }
    }
  }
}