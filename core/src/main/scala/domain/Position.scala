package domain

/** An entity that represents a Position
  *
  */
trait Position {
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
case class PositionImpl(override val latitude: Double, override val longitude: Double) extends Position