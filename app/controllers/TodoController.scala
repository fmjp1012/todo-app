package controllers

import model.Todo
import model.forms.TodoCreatingForm.todoCreatingForm
import model.forms.TodoEditingForm.todoEditingForm
import model.forms.TodoEditingInput
import persistence.repository.impl.{TodoCategoryRepositoryImpl, TodoRepositoryImpl}
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TodoController @Inject() (
  mcc:                    MessagesControllerComponents,
  todoRepository:         TodoRepositoryImpl,
  todoCategoryRepository: TodoCategoryRepositoryImpl
) extends MessagesAbstractController(mcc) {

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
          todoCategoryRepository
            .findById(todoCreatingInput.categoryId)
            .flatMap {
              case None               =>
                todoCategoryRepository.getAll.map(todoCategories =>
                  BadRequest(
                    views.html.todo.Create(
                      todoCreatingForm.withError("categoryId", "Invalid Todo Category Id"),
                      todoCategories
                    )
                  )
                )
              case Some(todoCategory) => {
                val newTodo: Todo#WithNoId = Todo(
                  None,
                  todoCategory.id,
                  todoCreatingInput.title,
                  todoCreatingInput.body,
                  todoCreatingInput.state
                ).toWithNoId

                todoRepository
                  .insert(newTodo)
                  .map(_ => Redirect(routes.TodoController.index()))
              }
            }
        }
      )
  }
  def editForm(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoRepository.findById(Todo.Id(id)) zip todoCategoryRepository.getAll map {
      case (None, _)                    => NotFound
      case (Some(todo), todoCategories) =>
        Ok(views.html.todo.Edit(todoEditingForm.fill(TodoEditingInput(todo)), todo, todoCategories))
    }
  }

  def edit(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    todoEditingForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          todoRepository.findById(Todo.Id(id)) zip todoCategoryRepository.getAll map {
            case (None, _)                    => NotFound
            case (Some(todo), todoCategories) =>
              BadRequest(views.html.todo.Edit(formWithErrors, todo, todoCategories))
          }
        },
        todoEditingInput => {
          todoRepository
            .findById(Todo.Id(id))
            .zip(
              todoCategoryRepository
                .findById(todoEditingInput.categoryId)
            )
            .flatMap {
              case (None, _)                        => Future.successful(NotFound)
              case (Some(todo), None)               =>
                todoCategoryRepository.getAll map { todoCategories =>
                  BadRequest(
                    views.html.todo.Edit(
                      todoEditingForm.fill(todoEditingInput).withError("categoryId", "Invalid Todo Category Id"),
                      todo,
                      todoCategories
                    )
                  )
                }
              case (Some(todo), Some(todoCategory)) => {
                val editedTodo: Todo#EmbeddedId = Todo(
                  Some(todo.v.id.get),
                  todoCategory.id,
                  todoEditingInput.title,
                  todoEditingInput.body,
                  todoEditingInput.state
                ).toEmbeddedId

                todoRepository
                  .update(editedTodo)
                  .map(_ => Redirect(routes.TodoController.index()))
              }
            }
        }
      )
  }
}
