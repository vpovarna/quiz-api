package org.example.quiz.dao.records

import org.example.quiz.entities.CategoryName

case class Category(id: Long = 0, name: String)

object Category {
  def fromRecord(record: CategoryName): Category = Category(name = record.name)
}
