package com.knoldus.userservice.data.persistence
import akka.actor.{Actor, ActorLogging}
import com.knoldus.userservice.data.model.{LoginUser, MasterUser, UserDetails}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


trait MasterUserComponent extends DB {

  class MasterUserTable(tag: Tag) extends Table[MasterUser](tag, "MASTER_USER") {
    def mobNumber: Rep[String] = column[String]("mob_number", O.PrimaryKey)

    def email: Rep[String] = column[String]("email")

    def name: Rep[String] = column[String]("name")

    def age: Rep[Int] = column[Int]("age")

    def isVerified = column[Boolean]("isVerified")

    def * = (mobNumber, email, name, age, isVerified).<>(MasterUser.tupled, MasterUser.unapply)
  }

  lazy val masterTableRef = TableQuery[MasterUserTable]

  class MasterUserRepository(implicit ec: ExecutionContext = ExecutionContext.global) {

    def validateEmail(email: String): Boolean = {
      import scala.util.matching.Regex
      val pattern = new Regex("[A-Za-z]{1,}[0-9]*.?[A-Za-z-0-9]*@[A-Za-z0-9.]+\\.[a-zA-Z]{2,3}$")
      (pattern.findFirstIn(email)) match {
        case Some(valid) => true
        case None => false
      }
    }

    def verifyEmail(email:String)=db.run{
      masterTableRef.filter(_.email === email).map(c => c.isVerified).update(true)
    }

    def all: Future[Seq[MasterUser]] = db.run {
      masterTableRef.result
    }

    def registerUser(user: MasterUser): Future[Int] = {
      db.run {
        masterTableRef += user
      }
    }
  }

}



