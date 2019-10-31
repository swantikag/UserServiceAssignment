package com.knoldus.userservice.model

import slick.jdbc.MySQLProfile.api._

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
}