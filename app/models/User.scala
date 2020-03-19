package models

import play.api.libs.json.OFormat
import reactivemongo.bson.BSONObjectID

object User {
  def apply(username: String,
            password: String
           ) = new User(BSONObjectID.generate(), username, password)
}

case class User(
                 _id: BSONObjectID,
                 username: String,
                 password: String,
                 highscore: Int = 0
               )

object JsonFormats {

  import play.api.libs.json.Json
  import reactivemongo.play.json._
  import reactivemongo.play.json.collection.JSONCollection

  implicit val userFormat: OFormat[User] = Json.format[User]
  implicit val loginDetailsFormat: OFormat[LoginDetails] = Json.format[LoginDetails]
}
