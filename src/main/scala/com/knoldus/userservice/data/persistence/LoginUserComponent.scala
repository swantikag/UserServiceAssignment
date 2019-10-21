package com.knoldus.userservice.data.persistence
import com.knoldus.userservice.data.model.LoginUser
import slick.jdbc.MySQLProfile.api._

trait LoginUserComponent extends MasterUserComponent {

  /**
   * The LOGIN_USER table holding login credentials of the registered user
   */

  class LoginUserTable(tag: Tag) extends Table[LoginUser](tag, "LOGIN_USER") {
    def mobNumber: Rep[String] = column[String]("mob_number")

    def uname: Rep[String] = column[String]("uname")

    def password: Rep[String] = column[String]("password")

    def * = (mobNumber, uname, password).<>(LoginUser.tupled, LoginUser.unapply)

    def fk_mobNumber = foreignKey("fk_MobNumber", mobNumber, masterTableRef)(_.mobNumber)
  }

  lazy val loginTableRef = TableQuery[LoginUserTable]
}
