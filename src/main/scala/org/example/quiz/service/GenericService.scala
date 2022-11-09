package org.example.quiz.service

import cats.effect._
import org.example.quiz.dao.GenericDao

class GenericService(dao: GenericDao) {

  def healthCheck: IO[String] =
    dao.testConnection().map { success =>
      s"Database Connectivity: ${if (success) "OK" else "FAILURE"}"
    }

}
