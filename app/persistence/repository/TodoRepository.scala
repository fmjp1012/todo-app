package persistence.repository

import ixias.slick.SlickRepository
import model.{Todo, TodoCategory}

import scala.concurrent.Future

trait TodoRepository extends SlickRepository[Todo.Id, Todo] {
  def getAll: Future[Seq[(Todo#EmbeddedId, TodoCategory#EmbeddedId)]]
  def insert(newTodo: Todo#WithNoId): Future[Todo.Id]
  def findById(id: Todo.Id): Future[Option[Todo#EmbeddedId]]
  def update(editedTodo: Todo#EmbeddedId): Future[Todo.Id]
}
