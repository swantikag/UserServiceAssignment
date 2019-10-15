package com.knoldus.userservice.data.persistence
import com.knoldus.userservice.data.model.{LoginRequest, LoginUser}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait LoginUserComponent extends DB with MasterUserComponent {

  class LoginUserTable(tag: Tag) extends Table[LoginUser](tag, "LOGIN_USER") {
    def mobNumber: Rep[String] = column[String]("mob_number")

    def uname: Rep[String] = column[String]("uname")

    def password: Rep[String] = column[String]("password")

    def * = (mobNumber, uname, password).<>(LoginUser.tupled, LoginUser.unapply)

    def fk_mobNumber = foreignKey("fk_MobNumber", mobNumber, masterTableRef)(_.mobNumber)
  }

  lazy val loginTableRef = TableQuery[LoginUserTable]

  class LoginUserRepository(implicit ec: ExecutionContext = ExecutionContext.global) {


    def insertLoginTable(user: LoginUser): Future[Int] = db.run {
      loginTableRef += user
    }

    def authenticateUser(user: LoginRequest):Future[Seq[LoginUser]]  = db.run{
      loginTableRef.filter(u => (u.uname === user.uname && u.password === user.password)).result
    }

    def isEmailVerified(user: LoginRequest):Future[Seq[Boolean]] = {
      val mobNumber = loginTableRef.filter(x => (x.uname === user.uname && x.password === user.password)).map(u => u.mobNumber)
      val isVerified = masterTableRef.map(_.isVerified)
      db.run(isVerified.result)
    }
  }
}
