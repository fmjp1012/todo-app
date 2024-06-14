package persistence.repository

import model.TodoCategory

import scala.concurrent.Future

trait TodoCategoryRepository {
  def getAll: Future[Seq[TodoCategory#EmbeddedId]]
  def findById(id: TodoCategory.Id): Future[Option[TodoCategory.EmbeddedId]]
}
