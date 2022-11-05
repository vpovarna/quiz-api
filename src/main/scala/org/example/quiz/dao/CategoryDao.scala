package org.example.quiz.dao

import scala.concurrent.{ExecutionContext, Future}

import io.getquill.{PostgresJAsyncContext, SnakeCase}
import org.example.quiz.dao.records.Category

trait CategoryDao {
  def save(category: Category): Future[Long]

  def all(): Future[Seq[Category]]

  def findById(id: Long): Future[Option[Category]]

  def deleteById(id: Long): Future[Boolean]
}

class CategoryDaoImpl(ctx: PostgresJAsyncContext[SnakeCase.type])(implicit
    ec: ExecutionContext
) extends CategoryDao {
  import ctx._

  private val categories = quote { query[Category] }

  override def save(category: Category): Future[Long] = {
    val q = quote {
      categories.insert(lift(category)).returningGenerated(_.id)
    }
    run(q)
  }

  override def all(): Future[Seq[Category]] = run(categories)

  override def findById(id: Long): Future[Option[Category]] = {
    val q = quote { categories.filter(_.id == lift(id)) }
    run(q).map(_.headOption)
  }

  override def deleteById(id: Long): Future[Boolean] = {
    val q = quote { categories.filter(_.id == lift(id)).delete }
    run(q).map(_ > 0)
  }
}
