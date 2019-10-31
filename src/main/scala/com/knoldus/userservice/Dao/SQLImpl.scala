package com.knoldus.userservice.Dao

import com.knoldus.userservice.config.DB

trait SQLImpl extends DB {
  val driver = slick.jdbc.MySQLProfile
  import driver.api._
  val db = Database.forConfig("mysql")
}
