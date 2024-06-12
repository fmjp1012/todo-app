package services
import model.TodoCategory
import persistence.repository.impl.TodoCategoryRepositoryImpl

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TodoCategoryService @Inject() (todoCategoryRepository: TodoCategoryRepositoryImpl) {
  def exists(id: TodoCategory.Id): Future[Boolean] = {
    todoCategoryRepository.findById(id).map {
      case None    => false
      case Some(_) => true
    }
  }
}
