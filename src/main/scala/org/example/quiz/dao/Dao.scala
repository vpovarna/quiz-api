package org.example.quiz.dao

import scala.concurrent.ExecutionContext

class Dao()(implicit ec: ExecutionContext) {

  val generic = new GenericDao()
}
