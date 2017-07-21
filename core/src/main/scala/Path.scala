/** A path (POI sequence) for a hunt
  *
  */
trait Path {

    /** Returns the list of POIs
      *
      * @return an immutable List
      */
    def getPois: scala.collection.immutable.List[POI]

}

/** A path (POI sequence) for a hunt
  *
  * @constructor create new Path from a list of Pois
  * @param pois immutable List of Pois
  */
class PathImpl(pois: scala.collection.immutable.List[POI]) extends Path {

    /** Returns the list of POIs
      *
      * @return an immutable List
      */
    override def getPois: List[POI] = {
        this.pois
    }
}