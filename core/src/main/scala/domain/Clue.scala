package domain

import play.api.libs.json.Json

/** An Entity that contains a Clue.
  *
  */
trait Clue extends Serializable {
    /**
      * Property to get the clue value.
      *
      * @return a String containing the Clue
      */
    def content: String

    /**
      * Property to set the clue value.
      *
      * @param content the value for the Clue
      */
    def content_=(content: String): Unit

}

/**
  * An Entity that contains a clue to reach the next POI in a Treasure Hunt
  *
  * @param content a string that contains the clue
  */
case class ClueImpl(override var content: String) extends Clue {

    implicit val clueWrites = Json.writes[ClueImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Clue {

    def apply(content: String): ClueImpl = {
        ClueImpl(content)
    }

}