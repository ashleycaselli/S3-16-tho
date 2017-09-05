package domain

import com.typesafe.scalalogging.Logger
import play.api.libs.json.{Json, Writes}

/**
  * A Treasure Hunt
  */
trait TreasureHunt extends Serializable {

    def ID: String

    def ID_=(ID: String): Unit

    def name: String

    def location: String

    def location_=(place: String): Unit

    def date: String

    def time: String

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
  * @param _ID    the Treasure Hunt ID
  * @param _teams Treasure Hunt's teams. If not specified is empty
  */
case class TreasureHuntImpl(private var _ID: String = null, override val name: String, private var _location: String = null, override val date: String, override val time: String, private var _teams: Seq[Team] = Seq.empty) extends TreasureHunt {

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

    implicit val treasureHuntWrites = new Writes[TreasureHuntImpl] {
        def writes(th: TreasureHuntImpl) = Json.obj(
            "ID" -> ID,
            "name" -> name,
            "location" -> location,
            "date" -> date,
            "time" -> time)
    }

    override def ID: String = _ID

    override def ID_=(ID: String): Unit = {
        require(ID != null)
        _ID = ID
    }

    override def location: String = _location

    override def location_=(place: String): Unit = {
        require(place != null)
        _location = place
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString
}

object TreasureHunt {

    def apply(ID: String = null, name: String, location: String, date: String, time: String): TreasureHuntImpl = TreasureHuntImpl(ID, name, location, date, time, null)

}
