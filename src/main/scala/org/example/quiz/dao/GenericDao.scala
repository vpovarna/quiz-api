package org.example.quiz.dao

import doobie.implicits._
import cats.effect.IO
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor

class GenericDao(xa: Transactor[IO]) {

  def testConnection(): IO[Boolean] = {
    val query = sql"SELECT 1".query[Int]
    val action = query.unique.map(_ == 1)
    action.transact(xa)
  }

}
