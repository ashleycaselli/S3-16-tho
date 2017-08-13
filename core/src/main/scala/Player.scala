/** A player of the treasure hunt
  *
  */
trait Player extends Positionable {

    /**
      * Property to get the Player's name
      *
      * @return the Player's name
      */
    def name: String

}

/**
  * A player of the treasure hunt
  *
  * @param name      the player's name
  * @param _position the player's position
  */
case class PlayerImpl(override val name: String, private var _position: Position = null) extends Player {

    /** Returns the position of the entity
      *
      * @return an option of entity position
      */
    override def position: Position = _position

    /**
      * Property to set the entity Position
      *
      * @param position new position to set
      */
    override def position_=(position: Position): Unit = {
        require(position != null)
        _position = position
    }

}