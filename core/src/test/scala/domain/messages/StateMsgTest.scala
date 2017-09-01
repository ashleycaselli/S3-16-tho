package domain.messages

import com.typesafe.scalalogging.Logger
import domain.StateImpl
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory

class StateMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Organizer"
    private val payload: String = new StateImpl(StateType.Start, "thID").defaultRepresentation
    private var startMessage: StateMsgImpl = null
    val logger = Logger(LoggerFactory.getLogger("test"))
    private val th = "TH-0"

    before {
        startMessage = StateMsgImpl(sender, payload)
    }

    test("StartMsg sender and payload must be String") {
        assert(startMessage.sender.isInstanceOf[String] && startMessage.payload.isInstanceOf[String])
    }

    test("StartMsg content checking") {
        assert(startMessage.sender === sender && startMessage.payload === payload)
    }

}
