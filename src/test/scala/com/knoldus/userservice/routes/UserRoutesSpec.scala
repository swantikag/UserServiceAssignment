package com.knoldus.userservice.routes

import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import com.knoldus.userservice.data.persistence.MasterUserComponent
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.userservice.repo.TestDBImpl
import akka.util.ByteString
import com.knoldus.userservice.data.model.{JsonSupport, LoginRequest, LoginUser, UserDetails}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

class UserRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MasterUserComponent with MockitoSugar with JsonSupport {

  val masterUserRepo = new MasterUserRepository with TestDBImpl

  val routesTest = new UserRoutes(masterUserRepo).routes

  val mockRepo = mock[MasterUserRepository]

  "The service" should {
    "register a new user with valid email ID to the database" in {

      when(mockRepo.registerUser(UserDetails("7546981235", "testing", "testing@gmail.com", 25, "test123", "test@123"))).thenReturn(Future[(Int,Int)]((1,1)))

      val jsonData = ByteString("""{"mobNumber":"7546981235","email":"testing@gmail.com","name":"tesing","age":25,"isVerified":1,"uname":"test123","password":"test@123"}""")

      val postRequest = HttpRequest(HttpMethods.POST,
        uri = "/registerUser",
        entity = HttpEntity(MediaTypes.`application/json`, jsonData))

      postRequest ~> routesTest ~> check {
        status.isSuccess() shouldEqual (true)
        responseAs[String] shouldEqual ("User Registered Successfully")
      }
    }
  }

  "The service" should {
      "not allow a registered user with unverified email ID to log in" in {
        val loginRequest = ByteString("""{"uname":"test123", "password":"test@123"}""")
        when(mockRepo.authenticateUser(LoginRequest("test123", "test@123"))).thenReturn(Future[Seq[LoginUser]](Seq(LoginUser("7894561235", "test123", "test@123"))))
        when(mockRepo.isEmailVerified(LoginRequest("test123", "test@123"))).thenReturn(Future[Seq[Boolean]](Seq(false)))
        val postRequest = HttpRequest(HttpMethods.POST,
          uri = "/loginUser",
          entity = HttpEntity(MediaTypes.`application/json`, loginRequest))
        postRequest ~> routesTest ~> check {
          status.isSuccess() shouldEqual true
          responseAs[String] shouldEqual ("Email ID for the user is not verified")
        }
      }
    }
    {
      "not allow a login request with wrong credentials to log in" in {
        val loginRequest = ByteString("""{"uname":"dummy", "password":"dummy@123"}""")
        //when(mockRepo.authenticateUser(LoginRequest("dummy", "dummy@123"))).thenReturn(Future[Seq[LoginUser]](Seq.empty))
        val postRequest = HttpRequest(HttpMethods.POST,
          uri = "/loginUser",
          entity = HttpEntity(MediaTypes.`application/json`, loginRequest))

        postRequest ~> routesTest ~> check {
          status.isSuccess() shouldEqual true
          responseAs[String] shouldEqual ("Invalid credentials")
        }
      }
    }

  "The service" should {
    "allow an registered email ID to be verified" in {
      //when(mockRepo.verifyEmail("testing@gmail.com")).thenReturn(Future[Int](1))
      Get("/verifyEmail?email=testing@gmail.com") ~> routesTest ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual("Email ID verified")
      }
    }

    "does not allow an unregistered email ID to be verified" in {
     // when(mockRepo.verifyEmail("demo@gmail.com")).thenReturn(Future[Int](0))
      Get("/verifyEmail?email=demo@gmail.com") ~> routesTest ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual("No user is registered with the given Email ID")
      }
    }

  }

  "The service" should {
    "allow a registered user with verified email to log in" in {
      val loginRequest = ByteString("""{"uname":"test123", "password":"test@123"}""")
      //when(mockRepo.authenticateUser(LoginRequest("test123", "test@123"))).thenReturn(Future[Seq[LoginUser]](Seq(LoginUser("7894561235", "test123", "test@123"))))
      //when(mockRepo.isEmailVerified(LoginRequest("test123", "test@123"))).thenReturn(Future[Seq[Boolean]](Seq(true)))
      val postRequest = HttpRequest(HttpMethods.POST,
        uri = "/loginUser",
        entity = HttpEntity(MediaTypes.`application/json`, loginRequest))
      postRequest ~> routesTest ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual ("Welcome,test123")
      }
    }
  }
}