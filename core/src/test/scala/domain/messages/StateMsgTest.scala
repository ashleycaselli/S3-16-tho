package domain.messages

import com.typesafe.scalalogging.Logger
import domain.messages.State.State
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class StateMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Organizer"
    private val start: String = State.Start.toString
    private val stop: String = State.Stop.toString
    private var startMessage: StartMsgImpl = null
    private var stopMessage: StopMsgImpl = null
    val logger = Logger(LoggerFactory.getLogger("test"))
    private val th = "TH-0"

    before {
        startMessage = StartMsgImpl(sender, treasureHuntID = th)
        stopMessage = StopMsgImpl(sender, treasureHuntID = th)
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
        val rep = startMessage.defaultRepresentation
        logger.info(rep)
        val json = Json.parse(rep)
        assert((json \ "sender").as[String] === sender)
        assert(((json \ "payload") \ "th").as[String] === th)
        assert(((json \ "payload") \ "state").as[State] === State.Start)
    }

    test("Checking StopMsg's serializability") {
        val rep = stopMessage.defaultRepresentation
        logger.info(rep)
        val json = Json.parse(rep)
        assert((json \ "sender").as[String] === sender)
        assert(((json \ "payload") \ "th").as[String] === th)
        assert(((json \ "payload") \ "state").as[State] === State.Stop)
    }

}
