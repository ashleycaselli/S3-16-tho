package domain

import org.scalatest.FunSuite

class TeamTest extends FunSuite {

    val name = "TestTeam"
    val players = Seq(PlayerImpl(name = "Mario", surname = "Super", email = "mario@super.bros"), PlayerImpl(name = "Luigi", surname = "Super", email = "luigi@super.bros"))
    val poi: POI = POIImpl(position = PositionImpl(45.467255, 9.1896145), name = "POI-0", treasureHuntID = 0)
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
        val newPlayer = PlayerImpl(name = "Peach", surname = "Super", email = "peach@super.bros")
        assert(team.players.isEmpty)
        team.addPlayer(newPlayer)
        assert(team.players.nonEmpty)
        val newPlayer2 = PlayerImpl(name = "Turtle", surname = "Super", email = "turtle@super.bros")
        team.addPlayer(newPlayer2)
        assert(team.players.contains(newPlayer))
        assert(team.players.contains(newPlayer2))
    }

}