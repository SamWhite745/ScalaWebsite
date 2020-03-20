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
    val userName = request.session.get("username")
    if (userName.isEmpty)
      Future.successful(Results.Redirect("/"))
    else
      mongoService.findByUsername(userName.get)
        .map(result => result)
        .flatMap(futureUserList =>
          block(new AuthenticatedRequest(futureUserList.head.username, request))
        )
  }

}
