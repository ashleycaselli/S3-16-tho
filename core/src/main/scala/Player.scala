/** A player of the treasure hunt
  *
  */
trait Player extends Positionable {

    /** Sets a new position of the player
      *
      * @param newPosition the new player position
      */
    def setPosition(newPosition: Position)

}

/** A player of the treasure hunt
  *
  */
class PlayerImpl extends Player {

    var position: Option[Position] = Option.empty[Position]

    /** Sets a new position of the player
      *
      * @param newPosition the new player position
      */
    override def setPosition(newPosition: Position) = {
        position = Some(newPosition)
    }

    /** Returns the position of the player
      *
      * @return an option of position
      */
    override def getPosition: Option[Position] = {
        position
    }
}