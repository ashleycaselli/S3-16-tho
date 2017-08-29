package domain.messages

import play.api.libs.json.{Json, Writes}

trait PositionMsg extends Message {
    def messageType = msgType.Position
}

case class PositionMsgImpl(override val sender: String, override val payload: String) extends PositionMsg {

    implicit val positionMsgWrites = new Writes[PositionMsgImpl] {
        def writes(positionMsg: PositionMsgImpl) = Json.obj(
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