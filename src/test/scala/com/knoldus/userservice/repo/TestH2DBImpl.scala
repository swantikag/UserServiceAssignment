package com.knoldus.userservice.repo

import com.knoldus.userservice.config.UserServiceConfig
import com.knoldus.userservice.data.persistence.DB
import org.slf4j.{Logger, LoggerFactory}

trait TestH2DBImpl extends DB {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override val driver = slick.jdbc.H2Profile

  import driver.api._

  val h2Url = "jdbc:h2:mem:test" +
  "DB_CLOSE_DELAY=-1" +
    ";MODE=MySql;" +
    "DATABASE_TO_UPPER=false;" +
    "INIT=runscript from 'src/test/resources/schema.sql'\\;runscript from 'src/test/resources/schemadata.sql'"

  override lazy val db: driver.backend.DatabaseDef = {
    logger.info("Creating test connection --------------------------------")
    Database.forURL(url=h2Url, driver="org.h2.Driver")
  }
}
