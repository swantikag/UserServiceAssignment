package com.knoldus.userservice.data.persistence
import com.knoldus.userservice.data.model.{LoginRequest, LoginUser, MasterUser, UserDetails}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext


trait MasterUserComponent {

  /**
   * MASTER_USER table holding details of the registered user
   */

  class MasterUserTable(tag: Tag) extends Table[MasterUser](tag, "MASTER_USER") {
    def mobNumber: Rep[String] = column[String]("mob_number", O.PrimaryKey)

    def email: Rep[String] = column[String]("email")

    def name: Rep[String] = column[String]("name")

    def age: Rep[Int] = column[Int]("age")

    def isVerified = column[Boolean]("isVerified")

    def * = (mobNumber, email, name, age, isVerified).<>(MasterUser.tupled, MasterUser.unapply)
  }

  lazy val masterTableRef = TableQuery[MasterUserTable]

  /**
   * Repository containing all methods required to access the Database
   * @param ec Implicit execution context
   */

  class MasterUserRepository(implicit ec:ExecutionContext) extends LoginUserComponent {
    this: DB =>

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

    /**
     * verifies email Id of the registered user by setting isVerified status to true
     * @param email is the email ID of the user
     * @return status of the DB query
     */

      def verifyEmail(email: String): Future[Int] = db.run {
        masterTableRef.filter(user => user.email === email).map(status => status.isVerified).update(true)
      }

    /**
     * DB query to get all registered users
     * @return list of all registered users
     */
      def all: Future[Seq[MasterUser]] = db.run {
        masterTableRef.result
      }

    /**
     * DB query to add details of the registered user to the database
     * @param user holds the details of the user registering
     * @return Status of DB insertion
     */

      def registerUser(user: UserDetails): Future[(Int, Int)] = {
        val loginTableRef = TableQuery[LoginUserTable]
        val action = for {
          ax1 <- (masterTableRef += MasterUser(user.mobNumber, user.email, user.name, user.age, false))
          ax2 <- (loginTableRef += LoginUser(user.mobNumber, user.uname, user.password))
        } yield {
          (ax1, ax2)
        }
        db.run(action)
      }

    /**
     * DB query to check the credentials used by the user to login
     * @param user holds the credentials for logging in
     * @return list of user details if the credentials are valid, else empty list
     */

      def authenticateUser(user: LoginRequest): Future[Seq[LoginUser]] = {
        val data = loginTableRef.filter(userObj => (userObj.uname === user.uname && userObj.password === user.password))
        db.run(data.result)
      }

    /**
     * DB query to check if the user accessing to login has verified email ID or not
     * @param user holds the credentials for logging in
     * @return List of current status of Email IDs (verified or not)
     */

      def isEmailVerified(user: LoginRequest): Future[Seq[Boolean]] = {
        val isVerified = loginTableRef.filter(x => (x.uname === user.uname && x.password === user.password))
          .map(_.mobNumber).flatMap(mob => masterTableRef.filter(x => x.mobNumber === mob).map(_.isVerified))
        db.run(isVerified.result)
      }
    }
  }



