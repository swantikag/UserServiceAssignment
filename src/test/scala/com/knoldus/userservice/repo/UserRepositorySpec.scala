import com.knoldus.userservice.data.model.{LoginRequest, LoginUser, MasterUser, UserDetails}
import com.knoldus.userservice.data.persistence.{DB, LoginUserComponent, MasterUserComponent}
import com.knoldus.userservice.repo.TestDBImpl
import org.scalatest.{AsyncFunSuite, FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepositorySpec extends FunSuite with MasterUserComponent with ScalaFutures {
  this:DB =>

//  override implicit def patienceConfig: PatienceConfig = PatienceConfig(5 seconds)

  implicit val defaultPatience = PatienceConfig(Span(5,Seconds), Span(500, Millis))

  val masterUserRepository = new MasterUserRepository with TestDBImpl

  test("Register a new user") {
    val testUser = UserDetails("8564123758", "TestData", "test@gmail.com", 42, "test", "test@123")
    val result = masterUserRepository.registerUser(testUser)
    whenReady(result) { status =>
      assert(status === (1,1))
    }
  }

//  test("Get Details of all users"){
//    val result = masterUserRepository.all
//    whenReady(result) { res =>
//      assert(res === Vector(MasterUser("8564123758", "test@gmail.com", "TestData", 42, false)))
//    }
//  }

  test("login user with wrong credentials") {
    val response = masterUserRepository.authenticateUser(LoginRequest("xyz","xyz"))
    whenReady(response){ result =>
      assert(result === Vector())
    }
  }

  test("login existing user") {
    val response = masterUserRepository.authenticateUser(LoginRequest("test", "test@123"))
    whenReady(response){ result =>
      assert(result === Vector(LoginUser("8564123758", "test", "test@123")))
    }
  }

  test("verify the email id of a registered user"){
    val response = masterUserRepository.verifyEmail("test@gmail.com")
    whenReady(response){ result =>
      assert(result === 1)
    }
  }

  test("check the current verification status of a registered user"){
    val response = masterUserRepository.isEmailVerified(LoginRequest("test", "test@123"))
    whenReady(response){ result =>
      assert(result === Vector(true))
    }
  }

  test("Malformed Email IDs are not registered") {
    val response = masterUserRepository.validateEmail("john..@gmail.com")
    assert(response === false)
  }
}
