package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit req =>
    Ok(views.html.Home())
  }
}
