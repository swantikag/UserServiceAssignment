package com.knoldus.userservice.config

import com.typesafe.config.ConfigFactory

trait UserServiceConfig {
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")
  private lazy val slickConfig = config.getConfig("mysql")

  lazy val applicationName: String = config.getString("application.name")
  lazy val httpHost = httpConfig.getString("interface")
  lazy val httpPort = httpConfig.getInt("port")

  lazy val jdbcUrl = slickConfig.getString("/properties/url")
  lazy val dbUser = slickConfig.getString("/properties/user")
  lazy val dbPassword = slickConfig.getString("/properties/password")
  lazy val dbDriver = slickConfig.getString("/properties/driver")
}
