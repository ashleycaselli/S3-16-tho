package domain.messages

import play.api.libs.json.{Json, Writes}

trait TreasureHuntMsg extends Message {
    def messageType = msgType.TreasureHunt
}

/**
  * A message that contains a quiz to unlock the next clue in a Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class TreasureHuntMsgImpl(override val sender: String, override val payload: String) extends TreasureHuntMsg {

    implicit val thMsgWrites = new Writes[TreasureHuntMsgImpl] {
        def writes(th: TreasureHuntMsgImpl) = Json.obj(
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
