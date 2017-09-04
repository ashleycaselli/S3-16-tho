package domain.messages

import play.api.libs.json.{Json, Writes}

/** A message that contains a Quiz.
  *
  */
trait ListTHsMsg extends Message {
    def messageType = msgType.ListTHs
}

/**
  * A message that contains a quiz to unlock the next clue in a Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class ListTHsMsgImpl(override val sender: String, override val payload: String) extends ListTHsMsg {

    implicit val listTHsMsgWrites = new Writes[ListTHsMsgImpl] {
        def writes(listTHsMsg: ListTHsMsgImpl) = Json.obj(
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
