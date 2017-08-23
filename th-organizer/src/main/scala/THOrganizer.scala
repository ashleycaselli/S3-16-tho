import com.typesafe.scalalogging.Logger
import domain.TreasureHuntImpl

sealed trait THOrganizer {

    def createTreasureHunt(name: String): Unit

}

class THOrganizerImpl(model: THModel) extends THOrganizer {

    private val logger = Logger[THOrganizerImpl]

    override def createTreasureHunt(name: String): Unit = {
        logger info (s"Creating a Treasure Hunt: $name ..........")
        model addTreasureHunt TreasureHuntImpl(name)
        logger info (s"$name creation successfully!")
    }

}
