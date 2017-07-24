/** A point of interest on the map
  *
  */
trait POI extends Positionable {

}

/** A point of interest on the map
  *
  */
class POIImpl(position: Position) extends POI {

    /** Returns the position of the point of interest
      *
      * @return an option of position
      */
    override def getPosition: Option[Position] = {
        Some(position)
    }
}