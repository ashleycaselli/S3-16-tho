package domain

import org.scalatest.FunSuite

class TeamTest extends FunSuite {

    val name = "TestTeam"
    val players = Seq(PlayerImpl("Player0"), PlayerImpl("Player1"))
    val poi: POI = new POIImpl(new PositionImpl(45.467255, 9.1896145), "POI-0", "TH-0")
    val team = TeamImpl(name, poi)

    test("Checking the Team's name") {
        assert(team.name === name)
    }

    test("Initially the team must have no player") {
        assert(team.players.isEmpty)
    }

    test("Checking the team creation using existing Players") {
        val newTeam = TeamImpl(name, poi, players)
        assert(newTeam.players === players)
    }

    test("Checking the Team's poi") {
        assert(team.poi === poi)
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