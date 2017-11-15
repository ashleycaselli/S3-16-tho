package domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.collection.mutable.ListBuffer

/** A point of interest on the map
  *
  */
trait POI extends Positionable with Serializable {

    /**
      * Property to get the POI's ID
      *
      * @return an Int that contains the POI's ID
      */
    def ID: Int

    /**
      * Property to set the POI's ID
      *
      * @param ID the POI's ID
      */
    def ID_=(ID: Int): Unit

    /**
      * Property to get the POI's name
      *
      * @return
      */
    def name: String

    /**
      * Property to get the Treasure Hunt's identifier
      *
      * @return
      */
    def treasureHuntID: Int

    /**
      * Property to get the POI's quiz
      *
      * @return
      */
    def quiz: Quiz

    /**
      * Property to set the POI's quiz
      *
      * @param quiz
      */
    def quiz_=(quiz: Quiz): Unit

    /**
      * Property to get the POI's clue
      *
      * @return
      */
    def clue: Clue

    /**
      * Property to set the POI's clue
      *
      * @param clue
      */
    def clue_=(clue: Clue): Unit
}

/**
  * A point of interest on the map
  *
  * @param position the POI's position
  * @param name     the POI's name
  * @param _quiz    a Quiz for POI. null if not specified
  * @param _clue    a Clue for POI. null if not specified
  */
case class POIImpl(private var _ID: Int = 0, override var position: Position, override val name: String, override val treasureHuntID: Int, private var _quiz: Quiz = null, private var _clue: Clue = null) extends POI {

    implicit val poiWrites = new Writes[POIImpl] {
        def writes(poi: POIImpl) = Json.obj(
            "ID" -> ID,
            "name" -> name,
            "treasureHuntID" -> treasureHuntID,
            "position" -> position.defaultRepresentation,
            "quiz" -> quiz.defaultRepresentation,
            "clue" -> clue.defaultRepresentation)
    }

    /**
      * Property to get the POI's ID
      *
      * @return an Int that contains the POI's ID
      */
    override def ID: Int = _ID

    /**
      * Property to set the POI's ID
      *
      * @param ID the POI's ID
      */
    override def ID_=(ID: Int): Unit = {
        require(ID >= 0)
        _ID = ID
    }

    /**
      * Property to get the POI's quiz
      *
      * @return the POI's quiz
      */
    override def quiz: Quiz = _quiz

    /**
      * Property to set the POI's quiz
      *
      * @param quiz the Quiz to be setted
      */
    override def quiz_=(quiz: Quiz): Unit = {
        require(quiz != null)
        _quiz = quiz
    }

    /**
      * Property to get the POI's clue
      *
      * @return the POI's clue
      */
    override def clue: Clue = _clue

    /**
      * Property to set the POI's clue
      *
      * @param clue
      */
    override def clue_=(clue: Clue): Unit = {
        require(clue != null)
        _clue = clue
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString
}


object POI {

    def apply(ID: Int, name: String, treasureHuntID: Int, position: String, quiz: String, clue: String): POIImpl = POIImpl(ID, Json.parse(position).as[Position], name, treasureHuntID, Json.parse(quiz).as[Quiz], Json.parse(clue).as[Clue])

    implicit val poiReads: Reads[POI] = (
        (JsPath \ "ID").read[Int] and
            (JsPath \ "name").read[String] and
            (JsPath \ "treasureHuntID").read[Int] and
            (JsPath \ "position").read[String] and
            (JsPath \ "quiz").read[String] and
            (JsPath \ "clue").read[String]
        ) (POI.apply _)

}

trait ListPOIs extends Serializable {
    /**
      * Property to get the list of POIs
      *
      * @return
      */
    def list: scala.collection.mutable.ListBuffer[POI]

}

/**
  * An Entity that contains a list of POIs
  *
  * @param list a string that contains the list
  */
case class ListPOIsImpl(override val list: ListBuffer[POI]) extends ListPOIs {

    implicit val listPOIsWrites = new Writes[ListPOIsImpl] {
        def writes(listPOIs: ListPOIsImpl) = Json.obj(
            "list" -> Json.toJson(list)(
                (list: ListBuffer[POI]) => JsArray(list.map(e => Json.toJson(e)(
                    (poi: POI) => Json.obj(
                        "ID" -> poi.ID,
                        "name" -> poi.name,
                        "treasureHuntID" -> poi.treasureHuntID,
                        "position" -> poi.position.defaultRepresentation,
                        "quiz" -> poi.quiz.defaultRepresentation,
                        "clue" -> poi.clue.defaultRepresentation)
                )))
            )
        )
    }


    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object ListPOIs {

    def apply(list: JsArray): ListPOIsImpl = ListPOIsImpl(list.as[ListBuffer[POI]])

    implicit val listPOIsReads: Reads[ListPOIs] = (JsPath \ "list").read[JsArray].map(ListPOIs.apply)

}