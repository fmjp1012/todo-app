/**
 *
 * to do sample project
 *
 */

package controllers

import javax.inject._
import play.api.mvc._
import lib.persistence.TodoRepository
import lib.model.Todo
import model.ViewValueHome

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents, todoRepository: TodoRepository) extends BaseController {

  def index() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    todoRepository.getAll.map(todos => Ok(views.html.todo.Index(vv, todos)))
  }
}
