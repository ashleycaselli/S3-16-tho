package domain

import com.typesafe.scalalogging.Logger

/** A Path for a treasure hunt
  *
  */
trait Path {

    /** Returns the list of POIs
      *
      * @return an immutable List
      */
    def POIs: List[POI]

    /**
      * Property to add a POI to the Path
      *
      * @param poi a POI to add to the Path
      */
    def addPOI(poi: POI): Unit

}

/**
  * A Path for a treasure hunt
  *
  * @param _pois a sequence of POI (must be not empty)
  */
class PathImpl(private var _pois: Seq[POI]) extends Path {

    require(!_pois.isEmpty && _pois != null)

    private val logger = Logger[Path]

    override def POIs: List[POI] = _pois.toList

    /**
      * Property to add a POI to the Path
      *
      * @param poi a POI to add to the Path
      */
    override def addPOI(poi: POI): Unit = {
        _pois = _pois :+ poi
        logger.info(s"${poi.name} added to the path")
    }

}