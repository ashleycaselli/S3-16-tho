package domain

import com.typesafe.scalalogging.Logger

/**
  * A Treasure Hunt
  */
trait TreasureHunt {

    /**
      * Property to get the Treasure Hunt's ID
      *
      * @return a String containing the ID value
      */
    def ID: String

    /**
      * Property to get Teams enrolled for Treasure Hunt
      *
      * @return an immutable List containing Teams
      */
    def teams: List[Team]

    /**
      * Property to add a Team to Treasure hunt
      *
      * @param team the Team to be added
      */
    def addTeam(team: Team): Unit

    /**
      * Property to get a Team by its name
      *
      * @param teamName name of the Team that is get
      */
    def team(teamName: String): Team

}

/**
  * A concrete implementation of a Treasure Hunt
  *
  * @param ID     the Treasure Hunt ID
  * @param _teams Treasure Hunt's teams. If not specified is empty
  */
case class TreasureHuntImpl(override val ID: String, private var _teams: Seq[Team] = Seq.empty) extends TreasureHunt {

    private val logger = Logger[Team]

    override def teams: List[Team] = {
        require(_teams != null)
        _teams toList
    }

    override def addTeam(team: Team): Unit = {
        _teams = _teams :+ team
        logger.info(s"${team.name} added to Treasure Hunt: $ID")
    }

    override def team(teamName: String): Team = _teams find (t => {
        t.name == teamName
    }) getOrElse {
        logger.info(s"$teamName is not enrolled at Treasure Hunt: $ID")
        null
    }

}
