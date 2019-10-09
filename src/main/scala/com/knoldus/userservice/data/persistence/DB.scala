package com.knoldus.userservice.data.persistence

import slick.jdbc.JdbcProfile

trait DB {
  val driver : JdbcProfile = slick.jdbc.MySQLProfile
  import driver.api._
  lazy val db = Database.forConfig("mysql")
}



