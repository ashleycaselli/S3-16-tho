package domain.messages

import play.api.libs.json.Json

trait PositionMsg extends Message{

}

case class PositionMsgImpl(override val sender: String, override val payload: String) extends PositionMsg{

    implicit val positionMsgWrites = Json.writes[PositionMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}