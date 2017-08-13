/** An entity that have got a position
  *
  */
trait Positionable {

    /** Returns the position of the entity
      *
      * @return a position
      */
    def position: Position

    /**
      * Property to set the entity Position
      *
      * @param position new position to set
      */
    def position_=(position: Position): Unit

}