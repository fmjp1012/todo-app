package controllers

import model.forms.{TodoCreatingForm, TodoCreatingInput}
import model.{Todo, TodoCategory}
import persistence.repository.impl.{TodoCategoryRepositoryImpl, TodoRepositoryImpl}
import play.api.data.Form
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject() (
    mcc:                    MessagesControllerComponents,
    todoRepository:         TodoRepositoryImpl,
    todoCategoryRepository: TodoCategoryRepositoryImpl
) extends MessagesAbstractController(mcc) {

  val todoCreatingForm: Form[TodoCreatingInput] = TodoCreatingForm.todoCreatingForm

  def index() = Action.async {
    todoRepository.getAll.map(todos => Ok(views.html.todo.Index(todos)))
  }

  def createForm() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCategoryRepository.getAll.map(todoCategories => Ok(views.html.todo.Create(todoCreatingForm, todoCategories)))
  }

  def create() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoCreatingForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          todoCategoryRepository.getAll.map(todoCategories =>
            BadRequest(
              views.html.todo.Create(formWithErrors, todoCategories)
            )
          )
        },
        todoCreatingInput => {
          val newTodo: Todo#WithNoId = Todo(
            None,
            TodoCategory.Id(todoCreatingInput.categoryId),
            todoCreatingInput.title,
            todoCreatingInput.body,
            Todo.Status(todoCreatingInput.state)
          ).toWithNoId
          todoRepository
            .insert(newTodo)
            .map(_ => Redirect(routes.TodoController.index()))
        }
      )
  }
}
