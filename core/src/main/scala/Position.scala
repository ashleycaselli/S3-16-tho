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
      * Property to set the latitude value.
      *
      * @param lat latitude value
      */
    def latitude_=(lat: Double): Unit

    /**
      * Property to get the longitude value.
      *
      * @return a Double containing longitude
      */
    def longitude: Double

    /**
      * Property to set the longitude value.
      *
      * @param lon longitude value
      */
    def longitude_=(lon: Double): Unit

}

/**
  * @param latitude  the position's latitude
  * @param longitude the position's longitude
  */
case class PositionImpl(override var latitude: Double, override var longitude: Double) extends Position