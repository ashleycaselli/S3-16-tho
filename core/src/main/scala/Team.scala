/** A group of players.
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
    def getPlayers: scala.collection.immutable.List[Player]

    /** Property to get the team's path.
      *
      * @return a path
      */
    def path: Path

    /**
      * Property to set the team's path.
      *
      * @param path
      */
    def path_=(path: Path): Unit
}

/** A group of players.
  *
  * @constructor create a new team with name members and a path
  * @param name       the name of the team
  * @param playerList the list of the members of the team
  * @param path       the path assigned to the team
  */
case class TeamImpl(override val name: String, playerList: List[Player], override var path: Path) extends Team {

    var players = playerList

    /** Returns a list of team's elements.
      *
      * @return an immutable list of Player
      */
    override def getPlayers: List[Player] = players

    /** Adds a player in this team.
      *
      * @param player the new player
      */
    def addPlayer(player: Player): Unit = players = players :+ player

}