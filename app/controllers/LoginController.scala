package controllers

import javax.inject.{Inject, Singleton}
import models.LoginDetails
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LoginController @Inject()(cc: ControllerComponents, val mongoService: MongoService) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def login(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(LoginDetails.loginForm))
  }

  def loginSubmit(): Action[AnyContent] = Action.async{ implicit request: Request[AnyContent] =>
    LoginDetails.loginForm.bindFromRequest.fold({ formWithErrors =>
      Future{BadRequest(views.html.login(formWithErrors))}
    }, { loginDetails =>
      mongoService.findByUsername(loginDetails.username).map(_.nonEmpty).map{ result =>
        if (result)
          Redirect(routes.HomeController.game()).withSession(request.session + ("username" -> loginDetails.username))
        else
          BadRequest("Incorrect username or password")
      }
    })
  }

}