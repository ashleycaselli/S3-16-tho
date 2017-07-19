
/** A point of interest on the map
  *
  */
trait Poi extends Positionable{

}



/** A point of interest on the map
  *
  * @constructor create a new POI with a position
  * @param position the position of the point
  */
class PoiImpl(position: Position) extends Poi{

  /** Returns the position of the point of interest
    *
    *  @return an option of position
    */
  override def getPosition: Option[Position] = {
    Some(position)
  }
}