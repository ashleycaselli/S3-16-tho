/** An entity that have got a position
  *
  */
trait Positionable {

    /** Returns the position of the entity
      *
      * @return an option of entity position
      */
    def getPosition: Option[Position]

}