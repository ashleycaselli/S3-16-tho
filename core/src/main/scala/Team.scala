
/** A group of players
  *
  */
trait Team{

  /** Returns the team's name
    *
    *  @return a String containing the name
    */
  def getName: String

  /** Returns a list of team's elements
    *
    *  @return an immutable list of Player
    */
  def getPlayers: scala.collection.immutable.List[Player]

  /** Returns the path of this team
    *
    *  @return a path
    */
  def getPath: Path
}



/** A group of players
  *
  * @constructor create a new team with name members and a path
  * @param name the name of the team
  * @param playerList the list of the members of the team
  * @param path the path assigned to the team
  */
class TeamImpl (name: String, playerList: List[Player], path: Path) extends Team{

  var players = playerList

  /** Returns the team's name
    *
    *  @return a String containing the name
    */
  override def getName: String = {
    name
  }

  /** Returns a list of team's elements
    *
    *  @return an immutable list of Player
    */
  override def getPlayers: List[Player] = {
    players
  }

  /** Returns the path of this team
    *
    *  @return a path
    */
  override def getPath: Path = {
    path
  }

  /** Adds a player in this team
    *
    *  @param player the new player
    */
  def addPlayer(player: Player){
    players = players :+ player
  }
}