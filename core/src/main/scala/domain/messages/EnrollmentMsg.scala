package domain.messages

import play.api.libs.json.Json

trait EnrollmentMsg extends Message {

}

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
