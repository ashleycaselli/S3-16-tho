package domain.messages

import play.api.libs.json.Json

/** An Entity that contains an Enrollment.
  *
  */
trait EnrollmentMsg extends Message {

}

/**
  * An Entity that contains an Enrollment to Treasure Hunt
  *
  * @param sender a string that contains the sender
  * @param payload a string that contains the payload
  */
case class EnrollmentMsgImpl(override val sender: String, override val payload: String) extends EnrollmentMsg {

    implicit val enrollmentMsgWrites = Json.writes[EnrollmentMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
