package org.example.quiz.service

import cats.effect._
import org.example.quiz.dao.GenericDao

class GenericService(dao: GenericDao) {

  def healthCheck: IO[String] =
    checkDbConnectivity().map { success =>
      s"Database Connectivity: ${if (success) "OK" else "FAILURE"}"
    }

  private def checkDbConnectivity(): IO[Boolean] =
    IO.fromFuture(IO(dao.testConnection()))
      .handleErrorWith(_ => IO(false))
}
