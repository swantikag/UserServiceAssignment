package com.knoldus.userservice.config

import com.typesafe.config.ConfigFactory

trait UserServiceConfig {
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")
  private lazy val slickConfig = config.getConfig("mysql")

  lazy val applicationName: String = config.getString("application.name")
  lazy val httpHost: String = httpConfig.getString("interface")
  lazy val httpPort: Int = httpConfig.getInt("port")

  lazy val jdbcUrl: String = slickConfig.getString("/properties/url")
  lazy val dbUser: String = slickConfig.getString("/properties/user")
  lazy val dbPassword: String = slickConfig.getString("/properties/password")
  lazy val dbDriver: String = slickConfig.getString("/properties/driver")
}
