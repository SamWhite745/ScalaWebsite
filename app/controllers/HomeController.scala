package controllers

import authentication.AuthenticationAction
import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, authAction: AuthenticationAction) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def register: Action[AnyContent]= Action {
    Ok(views.html.register())
  }

  def game: Action[AnyContent]= Action {
    Ok(views.html.game())
  }

}
