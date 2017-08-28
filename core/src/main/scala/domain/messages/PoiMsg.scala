package domain.messages

import play.api.libs.json.Json

trait PoiMsg extends Message {
    def messageType = msgType.Quiz
}

/**
  * A message that contains a quiz to unlock the next clue in a Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class PoiMsgImpl(override val sender: String, override val payload: String) extends PoiMsg {

    implicit val POIMsgWrites = Json.writes[PoiMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
