
/** A point of interest on the map
  *
  */
trait Poi extends Positionable {

}

/** A point of interest on the map
  *
  */
class PoiImpl(position: Position) extends Positionable {

    /** Returns the position of the point of interest
      *
      *  @return an option of position
      */
    override def getPosition: Option[Position] = {
        Some(position)
    }
}