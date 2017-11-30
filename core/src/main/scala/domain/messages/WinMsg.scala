package domain.messages

import play.api.libs.json.{Json, Writes}

trait WinMsg extends Message {
    def messageType = msgType.Unsubscription
}

/**
  * A message that contains team and treasure hunt ID to delete a subscription of a team
  *
  * @param sender  a string that contains the team ID
  * @param payload a string that contains the treasure hunt ID
  */
case class WinMsgImpl(override val sender: String, override val payload: String) extends WinMsg {

    implicit val WinMsgWrites = new Writes[WinMsgImpl] {
        def writes(win: WinMsgImpl) = Json.obj(
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
