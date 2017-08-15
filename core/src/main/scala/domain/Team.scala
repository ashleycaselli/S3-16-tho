package domain

import com.typesafe.scalalogging.Logger

import scala.collection.mutable.ListBuffer

/** A Team of the treasure hunt.
  *
  */
trait Team {

    /** Property to get the team's name.
      *
      * @return a String containing the name
      */
    def name: String

    /** Returns a list of team's elements.
      *
      * @return an immutable list of Player
      */
    def players: List[Player]

    /** Property to get the team's path.
      *
      * @return a path
      */
    def path: Path

    /**
      * Property to add a player to the team
      *
      * @param player
      */
    def addPlayer(player: Player): Unit
}

/**
  * A concrete Team representation.
  *
  * @param name team's name
  * @param path team's path
  * @param ps   team's players (empty if not specified)
  */
case class TeamImpl(override val name: String, override val path: Path, private val ps: Seq[Player] = ListBuffer.empty[Player]) extends Team {

    private var _players: Seq[Player] = ps
    private val logger = Logger[Team]

    /** Returns a list of team's elements.
      *
      * @return an immutable list of Player
      */
    override def players: List[Player] = _players toList

    /**
      * Property to add a player to the team
      *
      * @param player
      */
    override def addPlayer(player: Player): Unit = {
        _players = _players :+ player
        logger.info(s"${player.name} added to $name")
    }

}