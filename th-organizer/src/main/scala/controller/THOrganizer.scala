package controller

import com.typesafe.scalalogging.Logger
import domain._
import model.THModel

sealed trait THOrganizer {

    def createTreasureHunt(name: String, location: String, date: String, time: String): Unit

    def addPoi(position: Position, name: String, treasureHuntID: String, quiz: Quiz, clue: Clue): Unit

    def getPois(): Seq[POI]

    def getCode(): String

    def startHunt(): Unit
}

class THOrganizerImpl(model: THModel) extends THOrganizer {

    private val logger = Logger[THOrganizerImpl]

    override def createTreasureHunt(name: String, location: String, date: String, time: String): Unit = {
        logger info s"Creating a Treasure Hunt: $name"
        model addTreasureHunt TreasureHuntImpl(null, name, location, date, time)
        logger info s"$name creation successfully!"
    }

    override def addPoi(position: Position, name: String, treasureHuntID: String, quiz: Quiz, clue: Clue): Unit = {
        logger info s"Creating a POI: $name"
        model addPOI POIImpl(position, name, treasureHuntID, quiz, clue)
        logger info s"$name added to $treasureHuntID"
    }

    override def getPois(): Seq[POI] = {
        model getPOIs
    }

    override def getCode(): String = {
        model getCode
    }

    override def startHunt(): Unit = {
        model startHunt
    }
}
