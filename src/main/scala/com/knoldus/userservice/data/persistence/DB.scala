package com.knoldus.userservice.data.persistence

trait DB {
  val driver:slick.jdbc.JdbcProfile
  //= slick.jdbc.MySQLProfile
  val db: driver.backend.DatabaseDef
  //= Database.forConfig("mysql")
  def close: Unit = {
    db.close()
  }
}



