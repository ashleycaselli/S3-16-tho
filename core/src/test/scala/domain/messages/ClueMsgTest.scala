package domain.messages

import com.typesafe.scalalogging.Logger
import domain.{Clue, ClueImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class ClueMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Anakin Skywalker"
    private val clueText = "io sono tuo padre!"
    private var clue: Clue = null
    private var clueMessage: ClueMsg = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        clue = ClueImpl(clueText)
        clueMessage =  ClueMsgImpl(sender, clue.defaultRepresentation)
    }

    test("ClueMsg sender and payload must be String") {
        assert(clueMessage.sender.isInstanceOf[String] && clueMessage.payload.isInstanceOf[String])
    }

    test("ClueMsg content checking") {
        assert(clueMessage.sender === sender && clueMessage.payload === clue.defaultRepresentation)
    }

    test("Checking ClueMsg's serializability") {
        implicit val clueMsgReads = Json.reads[ClueMsgImpl]
        val newClueMsg = Json.parse(clueMessage.defaultRepresentation).as[ClueMsgImpl]
        logger.info(s"oldClueMsg: ${clueMessage.defaultRepresentation.toString}")
        logger.info(s"newClueMsg: ${newClueMsg.defaultRepresentation.toString}")
        assert(clueMessage === newClueMsg)
    }

}