import com.knoldus.userservice.Dao.UserDaoImpl
import com.knoldus.userservice.config.DB
import com.knoldus.userservice.model.{LoginRequest, LoginUser, UserDetails}
import com.knoldus.userservice.repo.TestDBImpl
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._

import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepositorySpec extends FunSuite with ScalaFutures {
  this:DB =>

  implicit val defaultPatience = PatienceConfig(Span(5,Seconds), Span(500, Millis))

  val userDoa = new UserDaoImpl with TestDBImpl

  test("Register a new user") {
    val testUser = UserDetails("8564123758", "TestData", "test@gmail.com", 42, "test", "test@123")
    val result = userDoa.registerUser(testUser)
    whenReady(result) { status =>
      assert(status === (1,1))
    }
  }

  test("login user with wrong credentials") {
    val response = userDoa.authenticateUser(LoginRequest("xyz","xyz"))
    whenReady(response){ result =>
      assert(result === Vector())
    }
  }

  test("login existing user") {
    val response = userDoa.authenticateUser(LoginRequest("test", "test@123"))
    whenReady(response){ result =>
      assert(result === Vector(LoginUser("8564123758", "test", "test@123")))
    }
  }

  test("verify the email id of a registered user"){
    val response = userDoa.verifyEmail("test@gmail.com")
    whenReady(response){ result =>
      assert(result === 1)
    }
  }

  test("check the current verification status of a registered user"){
    val response = userDoa.isEmailVerified(LoginRequest("test", "test@123"))
    whenReady(response){ result =>
      assert(result === Vector(true))
    }
  }
}
