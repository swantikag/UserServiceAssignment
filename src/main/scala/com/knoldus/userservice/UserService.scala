package com.knoldus.userservice

import com.knoldus.userservice.server.HttpServer
import com.knoldus.userservice.service.HttpService

object UserService extends App with HttpService with HttpServer
