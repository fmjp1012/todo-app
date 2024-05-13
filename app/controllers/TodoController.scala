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

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents, todoRepository: TodoRepository) extends BaseController {

  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val todos = Await.result(todoRepository.get(), 5.seconds)
    Ok(views.html.todo.Index(vv, todos))
  }
}
