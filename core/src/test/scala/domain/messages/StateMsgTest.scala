package domain.messages

import com.typesafe.scalalogging.Logger
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class StateMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Organizer"
    private var start: String = State.Start.toString
    private var stop: String = State.Stop.toString
    private var startMessage: StartMsgImpl = null
    private var stopMessage: StopMsgImpl = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        startMessage = StartMsgImpl(sender, start)
        stopMessage = StopMsgImpl(sender, stop)
    }

    test("StartMsg sender and payload must be String") {
        assert(startMessage.sender.isInstanceOf[String] && startMessage.payload.isInstanceOf[String])
    }

    test("StopMsg sender and payload must be String") {
        assert(stopMessage.sender.isInstanceOf[String] && stopMessage.payload.isInstanceOf[String])
    }

    test("StartMsg content checking") {
        assert(startMessage.sender === sender && startMessage.payload === start)
    }

    test("StopMsg content checking") {
        assert(stopMessage.sender === sender && stopMessage.payload === stop)
    }

    test("Checking StartMsg's serializability") {
        implicit val startMsgReads = Json.reads[StartMsgImpl]
        val newStartMsg = Json.parse(startMessage.defaultRepresentation).as[StartMsgImpl]
        logger.info(s"oldStartMsg: ${startMessage.defaultRepresentation.toString}")
        logger.info(s"newStartMsg: ${newStartMsg.defaultRepresentation.toString}")
        assert(startMessage === newStartMsg)
    }

    test("Checking StopMsg's serializability") {
        implicit val stopMsgReads = Json.reads[StopMsgImpl]
        val newStopMsg = Json.parse(stopMessage.defaultRepresentation).as[StopMsgImpl]
        logger.info(s"oldStopMsg: ${stopMessage.defaultRepresentation.toString}")
        logger.info(s"newStopMsg: ${newStopMsg.defaultRepresentation.toString}")
        assert(stopMessage === newStopMsg)
    }

}
