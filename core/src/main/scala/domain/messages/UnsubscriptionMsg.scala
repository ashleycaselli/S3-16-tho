package domain.messages

import play.api.libs.json.{Json, Writes}

trait UnsubscriptionMsg extends Message {
    def messageType = msgType.Unsubscription
}

/**
  * A message that contains team and treasure hunt ID to delete a subscription of a team
  *
  * @param sender  a string that contains the team ID
  * @param payload a string that contains the treasure hunt ID
  */
case class UnsubscriptionMsgImpl(override val sender: String, override val payload: String) extends UnsubscriptionMsg {

    implicit val UnsubMsgWrites = new Writes[UnsubscriptionMsgImpl] {
        def writes(unsub: UnsubscriptionMsgImpl) = Json.obj(
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