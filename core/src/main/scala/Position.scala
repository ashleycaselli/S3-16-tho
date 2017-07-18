/**
  * Created by CarmineVattimo on 18/07/2017.
  */

/** An Entity that contains the coordinates of a Position
  *
  */
trait Position {
    /**
      * getter of Latitude
      * @return a Double containing latitude value
      */
    def getLatitude: Double

    /**
      * getter of Longitude
      * @return a Double containing longitude
      */
    def getLongitude: Double
}

/**
  * @constructor create a new position with latitude and longitude
  * @param latitude the position's latitude
  * @param longitude the position's longitude
  */
class PositionImpl(latitude: Double, longitude: Double) extends Position {

    override def getLatitude: Double = {
        return latitude
    }

    override def getLongitude: Double = {
        return longitude
    }
}
