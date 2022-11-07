package org.example.quiz.dao

import scala.concurrent.{ExecutionContext, Future}

class GenericDao()(implicit
    ec: ExecutionContext
) {

  def testConnection(): Future[Boolean] = ???
//  {
  //    val q = quote { infix"SELECT 1".as[Int] }
  //    val result: Future[Index] = run(q)
  //    result.map(_ == 1)
  //  }

}
