package domain

/** A point of interest on the map
  *
  */
trait POI extends Positionable {
    /**
      * Property to get the POI's name
      *
      * @return
      */
    def name: String

    /**
      * Property to get the POI's quiz
      *
      * @return
      */
    def quiz: Quiz

    /**
      * Property to set the POI's quiz
      *
      * @param quiz
      */
    def quiz_=(quiz: Quiz): Unit

    /**
      * Property to get the POI's clue
      *
      * @return
      */
    def clue: Clue

    /**
      * Property to set the POI's clue
      *
      * @param clue
      */
    def clue_=(clue: Clue): Unit
}

/**
  * A point of interest on the map
  *
  * @param position the POI's position
  * @param name     the POI's name
  * @param _quiz    a Quiz for POI. null if not specified
  * @param _clue    a Clue for POI. null if not specified
  */
case class POIImpl(override var position: Position, override val name: String, private var _quiz: Quiz = null, private var _clue: Clue = null) extends POI {

    /**
      * Property to get the POI's quiz
      *
      * @return the POI's quiz
      */
    override def quiz: Quiz = _quiz

    /**
      * Property to set the POI's quiz
      *
      * @param quiz the Quiz to be setted
      */
    override def quiz_=(quiz: Quiz): Unit = {
        require(quiz != null)
        _quiz = quiz
    }

    /**
      * Property to get the POI's clue
      *
      * @return the POI's clue
      */
    override def clue: Clue = _clue

    /**
      * Property to set the POI's clue
      *
      * @param clue
      */
    override def clue_=(clue: Clue): Unit = {
        require(clue != null)
        _clue = clue
    }

}
