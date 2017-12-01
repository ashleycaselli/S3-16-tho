package domain.messages

import com.typesafe.scalalogging.Logger
import domain.StateImpl
import org.scalatest.{BeforeAndAfter, FunSuite}

class StateMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Organizer"
    private val payload: String = StateImpl(StateType.Start, 0).defaultRepresentation
    private var startMessage: StateMsgImpl = _
    val logger = Logger[StateMsgTest]
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
