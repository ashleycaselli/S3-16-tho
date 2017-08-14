package domain

import org.scalatest.FunSuite

class TeamTest extends FunSuite {

    val name = "TestTeam"
    val players = Seq(PlayerImpl("Player0"), PlayerImpl("Player1"))
    val path = new PathImpl(List(POIImpl(PositionImpl(44.147288, 12.236599), "domain.POI-1")))
    val team = TeamImpl(name, path)

    test("Checking the Team's name") {
        assert(team.name === name)
    }

    test("Initially the team must have no player") {
        assert(team.players.isEmpty)
    }

    test("Checking the team creation using existing Players") {
        val newTeam = TeamImpl(name, path, players)
        assert(newTeam.players === players)
    }

    test("Checking the Team's path") {
        assert(team.path === path)
    }

    test("Checking player addition to the team") {
        val newPlayer = PlayerImpl("Player2")
        assert(team.players.isEmpty)
        team.addPlayer(newPlayer)
        assert(!team.players.isEmpty)
        val newPlayer2 = PlayerImpl("Player3")
        team.addPlayer(newPlayer2)
        assert(team.players.contains(newPlayer))
        assert(team.players.contains(newPlayer2))
    }

}