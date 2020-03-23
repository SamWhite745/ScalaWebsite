package controllers

import authentication.AuthenticationAction
import javax.inject._
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter
import javax.inject.Inject
import play.api.http.MimeTypes
import play.api.mvc._
import play.api.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, authAction: AuthenticationAction, val mongoService: MongoService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def readAll(): Action[AnyContent] = Action.async {
    mongoService.findAll().map( listOfUsers =>
      Ok(listOfUsers.toString())
    )
  }

  def update(username: String, highscore: Int): Action[AnyContent] = Action {
    mongoService.updateHighscore(username, highscore)
    Ok("Updated")
  }

  def game: Action[AnyContent]= authAction {
    Ok(views.html.game())
  }


//  def javascriptRoutes: Action[AnyContent] = Action { implicit request =>
//    Ok(
//      JavaScriptReverseRouter("jsRoutes")(
//        routes.javascript.HomeController.update
//      )
//    ).as(MimeTypes.JAVASCRIPT)
//  }

}
