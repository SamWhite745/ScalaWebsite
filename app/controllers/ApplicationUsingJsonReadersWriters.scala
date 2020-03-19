package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.play.json._
import collection._
import models.User
import models.JsonFormats._
import play.api.libs.json.{JsValue, Json}
import reactivemongo.api.Cursor
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

class ApplicationUsingJsonReadersWriters @Inject()(
                                                    components: ControllerComponents,
                                                    val reactiveMongoApi: ReactiveMongoApi
                                                  ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("persons"))

  def create(user: User): Action[AnyContent] = Action.async {
    val futureResult = collection.flatMap(_.insert.one(user))
    futureResult.map(_ => Ok("User inserted"))
  }

  def createFromJson: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[User].map { user =>
      collection.flatMap(_.insert.one(user)).map { _ => Ok("User inserted")
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

}
