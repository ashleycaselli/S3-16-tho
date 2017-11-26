package domain

import play.api.libs.json.{JsPath, Json, Reads}

/** An entity that represents a Position
  *
  */
trait Login extends Serializable {
    /**
      * Property to get the latitude value.
      *
      * @return a Double containing latitude value
      */
    def username: String

    /**
      * Property to get the longitude value.
      *
      * @return a Double containing longitude
      */
    def password: String

}

/**
  * @param username the username
  * @param password the password
  */
case class LoginImpl(override val username: String, override val password: String) extends Login {

    implicit val loginWrites = Json.writes[LoginImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Login {

    def apply(username: String, password: String): Login = LoginImpl(username, password)

    import play.api.libs.functional.syntax._

    implicit val loginReads: Reads[Login] = (
      (JsPath \ "username").read[String] and
        (JsPath \ "password").read[String]
      ) (Login.apply _)

}