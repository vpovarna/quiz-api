package org.example.quiz.service

import org.example.quiz.dao.Dao

class Services(dao: Dao) {
  val generic = new GenericService(dao.generic)
  val category = new CategoryService(dao.category)
}
