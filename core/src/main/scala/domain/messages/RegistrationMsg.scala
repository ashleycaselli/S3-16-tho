package domain.messages

import play.api.libs.json.{Json, Writes}

/** An Entity that contains an Answer.
  *
  */
trait RegistrationMsg extends Message {
    def messageType = msgType.Login
}

/**
  * A message that represent the quiz-solved event
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class RegistrationMsgImpl(override val sender: String, override val payload: String = "") extends RegistrationMsg {

    implicit val registrationMsgWrites = new Writes[RegistrationMsg] {
        def writes(registrationMsg: RegistrationMsg) = Json.obj(
            "messageType" -> messageType,
            "sender" -> sender,
            "payload" -> payload)
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}