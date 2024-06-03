package persistence.repository

import ixias.slick.SlickRepository
import model.{Todo, TodoCategory}

import scala.concurrent.Future

trait TodoRepository extends SlickRepository[Todo.Id, Todo] {
  def getAll: Future[Seq[(Todo, TodoCategory)]]
  def insert(newTodo: Todo): Future[Int]
}
