package persistence.repository

import model.TodoCategory

import scala.concurrent.Future

trait TodoCategoryRepository {
  def getAll: Future[Seq[TodoCategory#EmbeddedId]]
  def findById(id: TodoCategory.Id): Future[Option[TodoCategory.EmbeddedId]]
  def insert(newTodoCategory: TodoCategory#WithNoId): Future[TodoCategory.Id]
  def update(editedTodoCategory: TodoCategory#EmbeddedId): Future[TodoCategory.Id]
  def delete(todoCategory: TodoCategory#EmbeddedId): Future[TodoCategory.Id]
}
