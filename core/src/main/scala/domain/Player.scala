package domain

/** A player of the treasure hunt
  *
  */
trait Player extends Positionable {

    /**
      * Property to get the Player's ID
      *
      * @return the Player's ID
      */
    def ID: Int

    /**
      * Property to set the Player's ID.
      *
      * @param ID a numeric Identifier
      */
    def ID_=(ID: Int): Unit

    /**
      * Property to get the Player's name
      *
      * @return the Player's name
      */
    def name: String

    /**
      * Property to get the Player's surname
      *
      * @return the Player's surname
      */
    def surname: String

    /**
      * Property to get the Player's email
      *
      * @return the Player's email
      */
    def email: String

}

/**
  * A player of the treasure hunt
  *
  * @param name      the player's name
  * @param _position the player's position
  */
case class PlayerImpl(private var _ID: Int = 0, override val name: String, override val surname: String, override val email: String, private var _position: Position = null) extends Player {

    /**
      * Property to get the Quiz's ID
      *
      * @return an Int that contains the Quiz's ID
      */
    override def ID: Int = _ID

    /**
      * Property to set the Quiz's ID
      *
      * @param ID the Quiz's ID
      */
    override def ID_=(ID: Int): Unit = {
        require(ID >= 0)
        _ID = ID
    }

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