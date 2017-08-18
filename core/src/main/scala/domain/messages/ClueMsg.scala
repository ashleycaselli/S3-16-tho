package domain.messages

import play.api.libs.json.Json

trait ClueMsg extends Message{

}

case class ClueMsgImpl(override val sender: String, override val payload: String) extends ClueMsg{

    implicit val clueMsgWrites = Json.writes[ClueMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
