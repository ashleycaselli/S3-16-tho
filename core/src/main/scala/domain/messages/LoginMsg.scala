package domain.messages

import play.api.libs.json.{Json, Writes}

/** An Entity that contains an Answer.
  *
  */
trait LoginMsg extends Message {
    def messageType = msgType.Login
}

/**
  * A message that represent the quiz-solved event
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class LoginMsgImpl(override val sender: String, override val payload: String = "") extends LoginMsg {

    implicit val loginMsgWrites = new Writes[LoginMsgImpl] {
        def writes(loginMsg: LoginMsgImpl) = Json.obj(
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