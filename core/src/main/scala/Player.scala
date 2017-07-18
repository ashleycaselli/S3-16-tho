
/** A player of the treasure hunt
  *
  */
trait Player extends Positionable{

  /** Sets a new position of the player
    *
    *  @param newPosition the new player position
    */
  def setPosition (newPosition: Position)

}