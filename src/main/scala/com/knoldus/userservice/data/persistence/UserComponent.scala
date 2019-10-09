package com.knoldus.userservice.data.persistence
import com.knoldus.userservice.data.model.MasterUser
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext

trait MasterUserComponent extends DB {

  class MasterUserTable(tag:Tag) extends Table[MasterUser](tag, "MASTER_USER"){
    def mobNumber=column[String]("mob_number",O.PrimaryKey)
    def email=column[String]("email")
    def * = (mobNumber, email).<>(MasterUser.tupled, MasterUser.unapply)
  }

  lazy val masterTableRef = TableQuery[MasterUserTable]

  class MasterUserRepository(implicit ec:ExecutionContext) {
    def all = db.run{
      masterTableRef.result
    }

    def registerUser(user : MasterUser) = db.run{
      masterTableRef += user
    }
  }
}
