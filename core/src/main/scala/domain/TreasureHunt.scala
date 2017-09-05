package domain


import com.typesafe.scalalogging.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * A Treasure Hunt
  */
trait TreasureHunt extends Serializable {

    def ID: Int

    def ID_=(ID: Int): Unit

    def name: String

    def location: String

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
case class TreasureHuntImpl(private var _ID: Int = 0, override val name: String, override val location: String, override val date: String, override val time: String, private var _teams: Seq[Team] = Seq.empty) extends TreasureHunt {


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
            "date" -> date.toString,
            "time" -> time)
    }

    override def ID: Int = _ID

    override def ID_=(ID: Int): Unit = {
        require(ID != null)
        _ID = ID
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString
}

object TreasureHunt {

    def apply(ID: Int = 0, name: String, location: String, date: String, time: String): TreasureHuntImpl = {
        TreasureHuntImpl(ID, name, location, date, time, null)
    }
}


trait ListTHs extends Serializable {
    /**
      * Property to get the list of treasure hunts
      *
      * @return
      */
    def list: scala.collection.immutable.List[TreasureHunt]

}

/**
  * An Entity that contains a list of Treasure Hunts
  *
  * @param list a string that contains the list
  */
case class ListTHsImpl(override val list: List[TreasureHunt]) extends ListTHs {

    implicit val listTHsWrites = new Writes[ListTHsImpl] {
        def writes(listTHs: ListTHsImpl) = Json.obj(
            "list" -> Json.toJson(list)(

                new Writes[List[TreasureHunt]] {
                    def writes(list: List[TreasureHunt]) = JsArray(list.map(e => Json.toJson(e)(

                        new Writes[TreasureHunt] {
                            def writes(th: TreasureHunt) = Json.obj(
                                "ID" -> th.ID,
                                "name" -> th.name,
                                "location" -> th.location,
                                "date" -> th.date,
                                "time" -> th.time)
                        }


                    )))
                }

            )
        )
    }


    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object ListTHs {

    implicit val thReads: Reads[TreasureHunt] = (
      (JsPath \ "ID").read[Int] and
        (JsPath \ "name").read[String] and
        (JsPath \ "location").read[String] and
        (JsPath \ "date").read[String] and
        (JsPath \ "time").read[String]
      ) (TreasureHunt.apply _)

    def apply(list: JsArray): ListTHsImpl = {
        ListTHsImpl(list.as[List[TreasureHunt]])
    }

}