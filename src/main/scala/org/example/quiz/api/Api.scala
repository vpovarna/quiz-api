package org.example.quiz.api

import org.example.quiz.service.Services

class Api(services: Services) {
  val generic = new GenericApi(services.generic)
}