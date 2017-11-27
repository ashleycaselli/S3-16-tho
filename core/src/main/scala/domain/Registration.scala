package domain

import play.api.libs.json.{JsPath, Json, Reads}

/** An entity that represents a Position
  *
  */
trait Registration extends Serializable {
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
case class RegistrationImpl(override val username: String, override val password: String) extends Registration {

    implicit val registrationWrites = Json.writes[RegistrationImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Registration {

    def apply(username: String, password: String): Registration = RegistrationImpl(username, password)

    import play.api.libs.functional.syntax._

    implicit val loginReads: Reads[Registration] = (
      (JsPath \ "username").read[String] and
        (JsPath \ "password").read[String]
      ) (Registration.apply _)

}