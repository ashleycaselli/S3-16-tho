package domain

import org.scalatest.FunSuite

class PlayerTest extends FunSuite {

    private val name = "Giovianni"
    private val surname = "Paracetamolo"
    private val email = "gian@paracetamolo.gov"
    private val position = PositionImpl(1.3, 4.9)
    private var player = PlayerImpl(name = name, surname = surname, email = email)

    test("Getting the player's name") {
        assert(player.name === name)
    }

    test("Getting the player's position") {
        assert(player.position === null)
        player = PlayerImpl(name = name, surname = surname, email = email, _position = position)
        assert(player.position === position)
    }

    test("Setting a new player's position") {
        val newPosition = PositionImpl(2.0, -3.0)
        player.position = newPosition
        assert(player.position !== position)
        assert(player.position === newPosition)
    }

}
