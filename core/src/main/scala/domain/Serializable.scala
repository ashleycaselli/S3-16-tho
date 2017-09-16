package domain

/** A Trait that allow some entity to be transferred (in a String or Json Representation).
  *
  */
trait Serializable {

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    def defaultRepresentation: String

}
