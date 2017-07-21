/** A Trait that allow some entity to be transfered (in a String or Json Representation)
  *
  */
trait Serializable {
    /**
      * getter of the representation
      *
      * @return a String containing the representation
      */
    def getDefaultRepresentation: String
}
