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
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global

class MongoService @Inject()(
                              val reactiveMongoApi: ReactiveMongoApi
                            ) extends ReactiveMongoComponents {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("persons"))

  def createUser(user: User): Future[WriteResult] = {
    collection.flatMap(_.insert.one(user))
  }

  def findAll(): Future[List[User]] = {
    collection.map {
      _.find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[User]()
    }.flatMap(
      _.collect[List](
        -1,
        Cursor.FailOnError[List[User]]()
      )
    )
  }

  def doesNotExist(username: String): Future[Boolean] = {
    findByUsername(username).map(user => user.isEmpty)
  }

  def findByUsername(username: String): Future[List[User]] =  {
    val cursor: Future[Cursor[User]] = collection.map {
      _.find(Json.obj("username" -> username)).
        sort(Json.obj("created" -> -1)).
        cursor[User]()
    }

    val futureUsersList: Future[List[User]] =
      cursor.flatMap(
        _.collect[List](
          -1,
          Cursor.FailOnError[List[User]]()
        )
      )

    futureUsersList
  }
}
