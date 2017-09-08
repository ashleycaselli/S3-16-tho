package domain.messages

import com.typesafe.scalalogging.Logger
import domain.{Clue, ClueImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.libs.json.Json

class ClueMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Anakin Skywalker"
    private val clueText = "io sono tuo padre!"
    private var clue: Clue = _
    private var clueMessage: ClueMsg = _
    val logger = Logger[ClueMsgTest]

    before {
        clue = ClueImpl(clueText)
        clueMessage = ClueMsgImpl(sender, clue.defaultRepresentation)
    }

    test("ClueMsg sender and payload must be String") {
        assert(clueMessage.sender.isInstanceOf[String] && clueMessage.payload.isInstanceOf[String])
    }

    test("ClueMsg content checking") {
        assert(clueMessage.sender === sender && clueMessage.payload === clue.defaultRepresentation)
    }

    test("Checking ClueMsg's serializability") {
        val newClueMsg = Json.parse(clueMessage.defaultRepresentation).as[Message]
        logger info s"oldClueMsg: ${clueMessage.defaultRepresentation.toString}"
        logger info s"newClueMsg: ${newClueMsg.defaultRepresentation.toString}"
        assert(clueMessage === newClueMsg)
    }

}