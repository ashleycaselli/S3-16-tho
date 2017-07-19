/**
  * Created by CarmineVattimo on 19/07/2017.
  */

/** A Trait that allow some entity to be transfered (in a String or Json Representation)
  *
  */
trait Serializable {
    /**
      * getter of the representation
      * @return a String containing the representation
      */
    def getDefaultRepresentation: String
}
