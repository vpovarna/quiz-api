package org.example.quiz.api

import org.example.quiz.service.Services

class Api(services: Services) {
  val generic = new GenericApi(services.generic)
  val categories = new CategoryApi(services.category)
  val questions = new QuestionApi(services.question)
  val answers = new AnswerApi(services.answer)
}
