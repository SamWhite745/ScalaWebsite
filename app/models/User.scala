package models

import play.api.libs.json.OFormat

case class User(
               name : String,
               highscore: Int
               )

object JsonFormats {
  import play.api.libs.json.Json
  implicit val userFormat: OFormat[User] = Json.format[User]
}
