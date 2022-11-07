package org.example.quiz.dao

import scala.concurrent.{ExecutionContext, Future}

import io.getquill.{PostgresJAsyncContext, SnakeCase}

class GenericDao(ctx: PostgresJAsyncContext[SnakeCase.type])(implicit
    ec: ExecutionContext
) {
  import ctx._

  def testConnection(): Future[Boolean] = {
    val q = quote { infix"SELECT 1".as[Int] }
    val result: Future[Index] = run(q)
    result.map(_ == 1)
  }

}
