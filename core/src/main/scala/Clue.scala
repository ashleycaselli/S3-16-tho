/** An Entity that contains a Clue.
  *
  */
trait Clue extends Serializable {
    /**
      * Property to get the clue value.
      *
      * @return a String containing the Clue
      */
    def clue: String

    /**
      * Property to set the clue value.
      *
      * @param clue the value for the Clue.
      */
    def clue_=(clue: String): Unit

}

/**
  * An Entity that contains a clue to reach the next POI in a Treasure Hunt
  *
  * @param clue a string that contains the clue
  */
case class ClueImpl(override var clue: String) extends Clue {

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = clue
}
