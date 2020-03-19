package controllers

import javax.inject.{Inject, Singleton}
import models.{LoginDetails, User}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
  class RegisterController @Inject()(cc: ControllerComponents, val mongoService: MongoService) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def register(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.register(LoginDetails.loginForm))
  }

  def registerSubmit(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    LoginDetails.loginForm.bindFromRequest.fold({ formWithErrors =>
      Future{BadRequest(views.html.register(formWithErrors))}
    }, { loginDetails =>
      mongoService.doesNotExist(loginDetails.username).map{ result =>
        if (result) {
          mongoService.createUser(User(loginDetails.username, loginDetails.password))
          Redirect(routes.HomeController.index())
        } else
          BadRequest("Not unique username")
      }

    })
  }
}
