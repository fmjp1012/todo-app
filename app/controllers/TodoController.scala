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

  def index() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("main.css", "todo/index.css"),
      jsSrc  = Seq("main.js")
    )
    todoRepository.getAll.map(todos => Ok(views.html.todo.Index(vv, todos)))
  }
}
