package domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

/** A point of interest on the map
  *
  */
trait POI extends Positionable with Serializable {
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
case class POIImpl(override var position: Position, override val name: String, override val treasureHuntID: Int, private var _quiz: Quiz = null, private var _clue: Clue = null) extends POI {

    implicit val poiWrites = new Writes[POIImpl] {
        def writes(poi: POIImpl) = Json.obj(
            "name" -> name,
            "treasureHuntID" -> treasureHuntID,
            "position" -> position.defaultRepresentation,
            "quiz" -> quiz.defaultRepresentation,
            "clue" -> clue.defaultRepresentation)
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

    def apply(name: String, treasureHuntID: Int, position: String, quiz: String, clue: String): POIImpl = POIImpl(Json.parse(position).as[Position], name, treasureHuntID, Json.parse(quiz).as[Quiz], Json.parse(clue).as[Clue])

    implicit val poiReads: Reads[POI] = (
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
    def list: scala.collection.immutable.List[POI]

}

/**
  * An Entity that contains a list of POIs
  *
  * @param list a string that contains the list
  */
case class ListPOIsImpl(override val list: List[POI]) extends ListPOIs {

    implicit val listPOIsWrites = new Writes[ListPOIsImpl] {
        def writes(listPOIs: ListPOIsImpl) = Json.obj(
            "list" -> Json.toJson(list)(
                (list: List[POI]) => JsArray(list.map(e => Json.toJson(e)(
                    (poi: POI) => Json.obj(
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

    def apply(list: JsArray): ListPOIsImpl = ListPOIsImpl(list.as[List[POI]])

    implicit val listPOIsReads: Reads[ListPOIs] = (JsPath \ "list").read[JsArray].map(ListPOIs.apply)

}