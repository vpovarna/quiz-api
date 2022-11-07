package org.example.quiz.dao

import scala.concurrent.ExecutionContext

import io.getquill.{PostgresJAsyncContext, SnakeCase}

class Dao(ctx: PostgresJAsyncContext[SnakeCase.type])(implicit
    ec: ExecutionContext
) {

  val generic = new GenericDao(ctx)
}
