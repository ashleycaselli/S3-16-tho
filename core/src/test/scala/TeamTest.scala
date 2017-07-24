import org.scalatest.FunSuite

class TeamTest extends FunSuite {

    val name = "TestTeam"
    val players: List[Player] = List(new PlayerImpl(), new PlayerImpl())
    val path: Path = new PathImpl(List(new POIImpl(PositionImpl(44.147288, 12.236599))))

    val team = TeamImpl(name, players, path)

    test("Checking the Team's name") {
        assert(team.name === name)
    }

    test("getPlayers of a team") {
        assert(team.getPlayers == players)
    }

    test("Checking the Team's path") {
        assert(team.path === path)
    }

    test("addPlayer in team") {
        val newPlayer = new PlayerImpl()
        team.addPlayer(newPlayer)
        assert(team.getPlayers === players :+ newPlayer)
    }

}