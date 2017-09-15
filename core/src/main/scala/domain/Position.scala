package domain

import play.api.libs.json.{JsPath, Json, Reads}

/** An entity that represents a Position
  *
  */
trait Position extends Serializable {
    /**
      * Property to get the latitude value.
      *
      * @return a Double containing latitude value
      */
    def latitude: Double

    /**
      * Property to get the longitude value.
      *
      * @return a Double containing longitude
      */
    def longitude: Double

}

/**
  * @param latitude  the position's latitude
  * @param longitude the position's longitude
  */
case class PositionImpl(override val latitude: Double, override val longitude: Double) extends Position {

    implicit val positionWrites = Json.writes[PositionImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Position {

    def apply(latitude: Double, longitude: Double): Position = PositionImpl(latitude, longitude)

    import play.api.libs.functional.syntax._

    implicit val positionReads: Reads[Position] = (
            (JsPath \ "latitude").read[Double] and
                    (JsPath \ "longitude").read[Double]
            ) (Position.apply _)

}