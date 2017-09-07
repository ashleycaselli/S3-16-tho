package controller


import com.typesafe.scalalogging.Logger
import domain._
import model.THModel

sealed trait THOrganizer {

    def createTreasureHunt(treasureHunt: TreasureHunt): Unit

    def addPoi(poi: POI): Unit

    def getPois(): Seq[POI]

    def getCode(): Int

    def startHunt(): Unit

    def getTreasureHunts(): List[TreasureHunt]
}

class THOrganizerImpl(model: THModel) extends THOrganizer {

    private val logger = Logger[THOrganizerImpl]

    override def createTreasureHunt(treasureHunt: TreasureHunt): Unit = {
        logger info s"Creating a Treasure Hunt: ${treasureHunt.name}"
        model addTreasureHunt TreasureHuntImpl(0, treasureHunt.name, treasureHunt.location, treasureHunt.date, treasureHunt.time)
        logger info s"${treasureHunt.name} creation successfully!"
    }

    override def addPoi(poi: POI): Unit = {
        logger info s"Creating a POI: ${poi.name}"
        model addPOI POIImpl(poi.position, poi.name, poi.treasureHuntID, poi.quiz, poi.clue)
        logger info s"${poi.name} added to ${poi.treasureHuntID}"
    }

    override def getPois(): Seq[POI] = {
        model getPOIs
    }

    override def getCode(): Int = {
        model getCode
    }

    override def startHunt(): Unit = {
        model startHunt
    }

    def getTreasureHunts(): List[TreasureHunt] = {
        model getTreasureHunts
    }
}
