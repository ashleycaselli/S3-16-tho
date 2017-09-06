package domain.messages

import play.api.libs.json.{Json, Writes}

/** A message that contains a Quiz.
  *
  */
trait ListPOIsMsg extends Message {
    def messageType = msgType.ListPOIs
}

/**
  * A message that contains a quiz to unlock the next clue in a Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class ListPOIsMsgImpl(override val sender: String, override val payload: String) extends ListPOIsMsg {

    implicit val listPOIsMsgWrites = new Writes[ListPOIsMsgImpl] {
        def writes(listPOIsMsg: ListPOIsMsgImpl) = Json.obj(
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
