package com.knoldus.userservice.config

trait DB {
  val driver:slick.jdbc.JdbcProfile
  val db: driver.backend.DatabaseDef
}
