package org.example.quiz.service

import cats.effect.IO
import org.example.quiz.dao.CategoryDao
import org.example.quiz.dao.records.Category
import org.example.quiz.entities.{CategoryEntity, CategoryName}

class CategoryService(dao: CategoryDao) {

  def get(id: Long): IO[Option[CategoryEntity]] = {
    dao.findById(id).map { maybeRecord: Option[Category] =>
      maybeRecord.map(CategoryEntity.fromRecord)
    }
  }

  def all: IO[Seq[CategoryEntity]] = {
    dao.getAll.map { someRecords =>
      someRecords.map(CategoryEntity.fromRecord)
    }
  }

  def addCategory(categoryName: CategoryName): IO[Long] =
    dao.save(categoryName)

  def deleteCategory(id: Long): IO[Boolean] =
    dao.deleteById(id)
}
