package domain

import play.api.libs.json.Json

trait Organizer extends Serializable {

    /** Property to get the organizer's ID.
      *
      * @return a Int containing the identifier
      */
    def idOrganizer: Int

    /** Property to get the organizer's name.
      *
      * @return a String containing the name
      */
    def name: String

    /** Property to get the organizer's surname.
      *
      * @return a String containing the surname
      */
    def surname: String

    /** Property to get the organizer's email.
      *
      * @return a String containing the email
      */
    def email: String

}

/**
  * A concrete Organizer representation.
  *
  * @param idOrganizer organizer's ID
  * @param name        organizer's name
  * @param surname     organizer's surname
  * @param email       organizer's email
  */
case class OrganizerImpl(override val idOrganizer: Int, override val name: String, override val surname: String, override val email: String) extends Organizer {

    implicit val organizerWrites = Json.writes[OrganizerImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Organizer {

    def apply(idOrganizer: Int, name: String, surname: String, email: String): Organizer = OrganizerImpl(idOrganizer, name, surname, email)

}