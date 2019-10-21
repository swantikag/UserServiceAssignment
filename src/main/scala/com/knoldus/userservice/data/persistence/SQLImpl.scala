package com.knoldus.userservice.data.persistence


trait SQLImpl extends DB {
  val driver = slick.jdbc.MySQLProfile
  import driver.api._
  val db = Database.forConfig("mysql")
}
