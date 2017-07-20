import org.scalatest.FunSuite

class TeamTest extends FunSuite{

  val name: String = "TestTeam"
  val players: List[Player] = List(new PlayerImpl() , new PlayerImpl() )
  val path: Path = new PathImpl(List(new PoiImpl(new PositionImpl(44.147288,12.236599))))

  val team = new TeamImpl(name, players, path)

  test("getName of a team") {
    assert(team.getName==name)
  }

  test("getPlayers of a team") {
    assert(team.getPlayers==players)
  }

  test("getPath of a team") {
    assert(team.getPath==path)
  }

  test("addPlayer in team") {
    val newPlayer=new PlayerImpl()
    team.addPlayer(newPlayer)
    assert(team.getPlayers==players:+newPlayer)
  }

}