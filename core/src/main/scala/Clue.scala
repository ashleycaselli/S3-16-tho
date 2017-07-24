/** An Entity that contains a clue to reach the next POI in a Treasure Hunt
  *
  */
trait Clue extends Serializable {
    /**
      * getter of the Clue
      *
      * @return a String containing the Clue
      */
    def getClue: String
}

/**
  * An Entity that contains a clue to reach the next POI in a Treasure Hunt
  *
  * @param clue a string that contains the clue
  */
class ClueImpl(clue: String) extends Clue {
    /**
      * getter of the Clue
      *
      * @return a String containing the Clue
      */
    override def getClue: String = {
        return clue
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        return clue
    }
}
