package domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

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
<<<<<<< HEAD
      * Property to get the hunt ID
=======
      * Property to get the Treasure Hunt's identifier
>>>>>>> 1ed98ec541691196a7531567e100f8b30adb5dfe
      *
      * @return
      */
    def treasureHuntID: String

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
case class POIImpl(override var position: Position, override val name: String, override val treasureHuntID: String, private var _quiz: Quiz = null, private var _clue: Clue = null) extends POI {

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

    implicit val positionReads: Reads[Position] = (
            (JsPath \ "latitude").read[Double] and
                    (JsPath \ "longitude").read[Double]
            ) (Position.apply _)

    implicit val quizReads: Reads[Quiz] = (
            (JsPath \ "question").read[String] and
                    (JsPath \ "answer").read[String]
            ) (Quiz.apply _)

    implicit val clueReads: Reads[Clue] =
        (JsPath \ "content").read[String].map(Clue.apply _)

    def apply(name: String, treasureHuntID: String, position: String, quiz: String, clue: String): POIImpl = {
        POIImpl(Json.parse(position).as[Position], name, treasureHuntID, Json.parse(quiz).as[Quiz], Json.parse(clue).as[Clue])
    }

}
