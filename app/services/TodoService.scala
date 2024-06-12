package services
import model.Todo
import persistence.repository.impl.TodoRepositoryImpl

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TodoService @Inject() (todoRepository: TodoRepositoryImpl) {
  def exists(id: Todo.Id): Future[Boolean] = {
    todoRepository.findById(id).map {
      case None    => false
      case Some(_) => true
    }
  }
}
