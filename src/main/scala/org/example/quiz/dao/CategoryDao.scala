package org.example.quiz.dao

import cats.effect._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.example.quiz.dao.records.Category

trait CategoryDao {
  def save(category: Category): IO[Long]
  def getAll: IO[Seq[Category]]
  def findById(id: Long): IO[Option[Category]]
  def deleteById(id: Long): IO[Boolean]
}

class CategoryDaoImpl(xa: Transactor[IO]) extends CategoryDao {

  override def save(category: Category): IO[Long] = {
    val update =
      sql"insert into category (name) values (${category.name})".update
    val action = update.withUniqueGeneratedKeys[Long]("id")
    action.transact(xa)
  }

  override def getAll: IO[Seq[Category]] = {
    val query = sql"select * from category".query[Category]
    query.stream.compile.toList.transact(xa)
  }

  override def findById(id: Long): IO[Option[Category]] = {
    val query =
      sql"select * from category where id=$id".query[Category]
    val action = query.option
    action.transact(xa)
  }

  override def deleteById(id: Long): IO[Boolean] = {
    val update = sql"delete from category where id = $id".update
    update.run.transact(xa).map(_ == 1)
  }
}
