package domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

/** An Entity that contains a Clue.
  *
  */
trait Clue extends Serializable {


    /**
      * Property to get the clue ID.
      *
      * @return a Int containing the Clue ID
      */
    def ID: Int

    /**
      * Property to set the clue ID.
      *
      * @param ID the value for the Clue ID
      */
    def ID_=(ID: Int): Unit

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
case class ClueImpl(private var _ID: Int = 0, override var content: String) extends Clue {

    implicit val clueWrites = new Writes[ClueImpl] {
        def writes(quiz: ClueImpl) = Json.obj(
            "ID" -> ID,
            "content" -> content)
    }

    /**
      * Property to get the Clue's ID
      *
      * @return an Int that contains the Clue's ID
      */
    override def ID: Int = _ID

    /**
      * Property to set the Clue's ID
      *
      * @param ID the Clue's ID
      */
    override def ID_=(ID: Int): Unit = {
        require(ID >= 0)
        _ID = ID
    }


    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Clue {

    def apply(ID: Int, content: String): ClueImpl = ClueImpl(ID, content)

    implicit val clueReads: Reads[Clue] = (
        (JsPath \ "ID").read[Int] and
            (JsPath \ "content").read[String]
        ) (Clue.apply _)

}