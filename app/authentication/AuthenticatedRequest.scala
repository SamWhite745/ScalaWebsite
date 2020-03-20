package authentication

import com.google.inject.Inject
import controllers.MongoService
import models.LoginDetails
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedRequest[A](val username: String, request: Request[A]) extends WrappedRequest[A](request)

class AuthenticationAction @Inject()(val parser: BodyParsers.Default, val mongoService: MongoService)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] {

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {
    request.session.get("username")
      .map(username => mongoService.findByUsername(username))
      .map(futureUser => futureUser.map{ userList =>
        userList.map(user => block(new AuthenticatedRequest(user.username, request)))})
      .getOrElse(Future.successful(Results.Redirect("/")))
  }

}
