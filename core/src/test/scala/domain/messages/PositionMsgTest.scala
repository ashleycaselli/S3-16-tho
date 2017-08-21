package domain.messages

import com.typesafe.scalalogging.Logger
import domain.{Position, PositionImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class PositionMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Elizabeth Alexandra Mary"
    private val latitude = 51.5007291
    private val longitude = -0.1246255
    private var position: Position = null
    private var positionMessage: PositionMsg = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        position = PositionImpl(latitude, longitude)
        positionMessage =  PositionMsgImpl(sender, position.defaultRepresentation)
    }

    test("PositionMsg sender and payload must be String") {
        assert(positionMessage.sender.isInstanceOf[String] && positionMessage.payload.isInstanceOf[String])
    }

    test("PositionMsg content checking") {
        assert(positionMessage.sender === sender && positionMessage.payload === position.defaultRepresentation)
    }

    test("Checking PositionMsg's serializability") {
        implicit val positionMsgReads = Json.reads[PositionMsgImpl]
        val newPositionMsg = Json.parse(positionMessage.defaultRepresentation).as[PositionMsgImpl]
        logger.info(s"oldPositionMsg: ${positionMessage.defaultRepresentation.toString}")
        logger.info(s"newPositionMsg: ${newPositionMsg.defaultRepresentation.toString}")
        assert(positionMessage === newPositionMsg)
    }

}