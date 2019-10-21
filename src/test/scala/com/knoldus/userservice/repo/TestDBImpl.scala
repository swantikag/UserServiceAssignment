package com.knoldus.userservice.repo

import com.knoldus.userservice.data.persistence.DB


trait TestDBImpl extends DB {
  val driver = slick.jdbc.H2Profile
  import driver.api._

  val db: driver.backend.DatabaseDef = Database.forConfig("h2memTry")
}
