package controller

import com.typesafe.scalalogging.Logger
import domain._
import model.THModel

sealed trait THOrganizer {

    def createTreasureHunt(name: String): Unit

    def addPoi(position: Position, name: String, treasureHuntID: String, quiz: Quiz, clue: Clue): Unit
}

class THOrganizerImpl(model: THModel) extends THOrganizer {

    private val logger = Logger[THOrganizerImpl]

    override def createTreasureHunt(name: String): Unit = {
        logger info s"Creating a Treasure Hunt: $name"
        model addTreasureHunt TreasureHuntImpl(name)
        logger info s"$name creation successfully!"
    }

    override def addPoi(position: Position, name: String, treasureHuntID: String, quiz: Quiz, clue: Clue): Unit = {
        logger info s"Creating a POI: $name"
        model addPOI POIImpl(position, name, treasureHuntID, quiz, clue)
        logger info s"$name added to $treasureHuntID"
    }
}
