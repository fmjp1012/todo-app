package controllers

import model.Todo
import model.forms.{TodoCreatingForm, TodoCreatingInput}
import persistence.repository.impl.{TodoCategoryRepositoryImpl, TodoRepositoryImpl}
import play.api.data.Form
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents, todoRepository: TodoRepository) extends BaseController {

  val todoCreatingForm: Form[TodoCreatingInput] = TodoCreatingForm.todoCreatingForm

  def index() = Action.async {
    todoRepository.getAll.map(todos => Ok(views.html.todo.Index(todos)))
  }

  def createForm() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryRepository.getAll.map(todoCategories => Ok(views.html.todo.Create(todoCreatingForm, todoCategories)))
  }
  }
}
