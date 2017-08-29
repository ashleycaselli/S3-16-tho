package domain.messages

import play.api.libs.json.{Json, Writes}

/** An Entity that contains an Enrollment.
  *
  */
trait EnrollmentMsg extends Message {
    def messageType = msgType.Enrollment
}

/**
  * An Entity that contains an Enrollment to Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class EnrollmentMsgImpl(override val sender: String, override val payload: String) extends EnrollmentMsg {

    implicit val enrollmentMsgWrites = new Writes[EnrollmentMsgImpl] {
        def writes(enrollmentMsg: EnrollmentMsgImpl) = Json.obj(
            "messageType" -> messageType,
            "sender" -> sender,
            "payload" -> payload)
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
