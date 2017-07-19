/**
  * Created by CarmineVattimo on 19/07/2017.
  */

/** An Entity that contains a clue to reach the next POI in a Treasure Hunt
  *
  */
trait Clue extends Serializable {
    /**
      * getter of the Clue
      * @return a String containing the Clue
      */
    def getClue: String
}

/**
  * An Entity that contains a clue to reach the next POI in a Treasure Hunt
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
      * getter of the representation
      *
      * @return a String containing the representation
      */
    override def getDefaultRepresentation: String = {
        return clue
    }
}
