package domain.messages

import play.api.libs.json.Json

/** A message that contains a Clue.
  *
  */
trait ClueMsg extends Message{

}

/**
  * A message that contains a clue to reach the next POI in a Treasure Hunt
  *
  * @param sender a string that contains the sender
  * @param payload a string that contains the payload
  */
case class ClueMsgImpl(override val sender: String, override val payload: String) extends ClueMsg {

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
