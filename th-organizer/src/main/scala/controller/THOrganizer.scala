package controller

import com.lynden.gmapsfx.javascript.`object`.Marker
import com.typesafe.scalalogging.Logger
import domain._
import model.THModel

/**
  * This is the Controller layer of the application.
  */
sealed trait THOrganizer {

    def createTreasureHunt(treasureHunt: TreasureHunt): Unit

    def addPoi(poi: POI, poiMarker: Marker): Unit

    def deletePoi(poi: POI): Unit

    def getPois: Seq[POI]

    def addPoiMarker(poiMarker: Marker, poi: POI): Unit

    def getPoiMarker(poi: POI): Option[Marker]

    def getCode: Int

    def startHunt(): Unit

    def isTHRunning(): Boolean

    def setTHRunning(ID: Int): Unit

    def stopHunt(): Unit

    def model: THModel

    def getTreasureHunts: List[TreasureHunt]
}

/**
  * Class that implements a Controller.
  *
  * @param _model
  */
class THOrganizerImpl(_model: THModel) extends THOrganizer {

    private val logger = Logger[THOrganizerImpl]

    override def createTreasureHunt(treasureHunt: TreasureHunt): Unit = {
        logger info s"Creating a Treasure Hunt: ${treasureHunt.name}"
        _model addTreasureHunt treasureHunt
        logger info s"${treasureHunt.name} creation successfully!"
    }

    override def addPoi(poi: POI, poiMarker: Marker): Unit = {
        logger info s"Creating a POI: ${poi.name}"
        _model addPOI (poi)
        _model addPoiMarker(poiMarker, poi)
        logger info s"${poi.name} added to ${poi.treasureHuntID}"
    }

    override def deletePoi(poi: POI): Unit = {
        _model deletePOI (poi)
        logger info s"POI ${poi.name} deleted"
    }

    override def addPoiMarker(poiMarker: Marker, poi: POI): Unit = {
        _model addPoiMarker(poiMarker, poi)
    }

    override def getPoiMarker(poi: POI): Option[Marker] = {
        _model getPoiMarker (poi)
    }

    override def getPois: Seq[POI] = _model getPOIs

    override def getCode: Int = _model getCode

    override def model: THModel = _model

    override def startHunt(): Unit = _model startHunt()

    override def stopHunt(): Unit = _model stopHunt()

    override def isTHRunning(): Boolean = _model isTHRunning()

    override def setTHRunning(ID: Int): Unit = _model setRunningTH (ID)

    def getTreasureHunts: List[TreasureHunt] = _model getTreasureHunts

}

object THOrganizer {

    private var _instance: THOrganizer = _

    def apply(model: THModel): THOrganizer = {
        _instance match {
            case null => {
                _instance = new THOrganizerImpl(model)
                _instance
            }
            case _ => _instance
        }
    }

    def instance: THOrganizer = _instance

}
